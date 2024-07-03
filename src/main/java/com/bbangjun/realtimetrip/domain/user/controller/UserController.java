package com.bbangjun.realtimetrip.domain.user.controller;

import com.bbangjun.realtimetrip.domain.user.dto.LoginRequestDto;
import com.bbangjun.realtimetrip.domain.user.dto.SignUpRequestDto;
import com.bbangjun.realtimetrip.domain.user.dto.UserInfoResponseDto;
import com.bbangjun.realtimetrip.domain.user.service.UserService;
import com.bbangjun.realtimetrip.global.response.BaseResponse;
import com.bbangjun.realtimetrip.global.response.ResponseCode;
import com.bbangjun.realtimetrip.global.response.ResponseException;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
@Tag(name = "UserController", description = "유저 관련 API")
public class UserController {

    private final UserService userService;

    // API: 회원가입
    @PostMapping(value = "/signup", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public BaseResponse<Object> signUp(@ModelAttribute SignUpRequestDto signUpRequestDto) {
        try {
            return new BaseResponse<>(userService.signUp(signUpRequestDto));
        } catch (Exception e) {
            return new BaseResponse<>(ResponseCode.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    // API: 로그인
    @PostMapping("/login")
    public BaseResponse<Object> login(@RequestBody LoginRequestDto loginRequestDto, HttpServletResponse response) {

        try{
            UserInfoResponseDto userInfoResponseDto = userService.login(loginRequestDto.getEmail(), loginRequestDto.getPassword());

            if (userInfoResponseDto != null) {
                Cookie emailCookie = new Cookie("email", userInfoResponseDto.getEmail());
                Cookie nickNameCookie = new Cookie("nickName", userInfoResponseDto.getNickname());

                emailCookie.setMaxAge(7 * 24 * 60 * 60);
                nickNameCookie.setMaxAge(7 * 24 * 60 * 60);

                emailCookie.setPath("/");
                nickNameCookie.setPath("/");

                response.addCookie(emailCookie);
                response.addCookie(nickNameCookie);

                return new BaseResponse<>(userInfoResponseDto);
            } else {
                return new BaseResponse<>(ResponseCode.INCORRECT_LOGIN_INFO);
            }

        }catch (ResponseException e) {
            return new BaseResponse<>(e.getErrorCode(), e.getMessage());
        } catch (Exception e) {
            return new BaseResponse<>(ResponseCode.INTERNAL_SERVER_ERROR, e.getMessage());
        }

    }
}
