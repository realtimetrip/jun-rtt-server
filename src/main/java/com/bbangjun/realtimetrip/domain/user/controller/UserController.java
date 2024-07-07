package com.bbangjun.realtimetrip.domain.user.controller;

import com.bbangjun.realtimetrip.domain.user.dto.LoginRequestDto;
import com.bbangjun.realtimetrip.domain.user.dto.SignUpRequestDto;
import com.bbangjun.realtimetrip.domain.user.dto.UserInfoResponseDto;
import com.bbangjun.realtimetrip.domain.user.service.UserService;
import com.bbangjun.realtimetrip.global.response.BaseResponse;
import com.bbangjun.realtimetrip.global.response.ResponseCode;
import com.bbangjun.realtimetrip.global.response.ResponseException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.net.URLEncoder;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
@Tag(name = "UserController", description = "유저 관련 API")
public class UserController {

    private final UserService userService;

    // API: 회원가입
    @PostMapping(value = "/signup", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "회원가입", description = "유저의 회원가입을 진행합니다.")
    public BaseResponse<Object> signUp(@ModelAttribute SignUpRequestDto signUpRequestDto) {
        try {
            return new BaseResponse<>(userService.signUp(signUpRequestDto));
        } catch (Exception e) {
            return new BaseResponse<>(ResponseCode.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    // API: 로그인
    @PostMapping("/login")
    @Operation(summary = "로그인", description = "유저의 로그인을 진행합니다.")
    public BaseResponse<Object> login(@RequestBody LoginRequestDto loginRequestDto, HttpServletResponse response) {

        try{
            UserInfoResponseDto userInfoResponseDto = userService.login(loginRequestDto.getEmail(), loginRequestDto.getPassword());

            if (userInfoResponseDto != null) {
                Cookie emailCookie = new Cookie("email", userInfoResponseDto.getEmail());

                // 쿠키가 ASCII 문자만을 허용하므로, 닉네임을 인코딩하여 쿠키에 저장
                String encodedNickName = URLEncoder.encode(userInfoResponseDto.getNickName(), "UTF-8");
                Cookie nickNameCookie = new Cookie("nickName", encodedNickName);

                Cookie profileCookie = new Cookie("profile", userInfoResponseDto.getProfile());


                emailCookie.setMaxAge(7 * 24 * 60 * 60);
                nickNameCookie.setMaxAge(7 * 24 * 60 * 60);
                profileCookie.setMaxAge(7 * 24 * 60 * 60);

                emailCookie.setPath("/");
                nickNameCookie.setPath("/");
                profileCookie.setPath("/");

                response.addCookie(emailCookie);
                response.addCookie(nickNameCookie);
                response.addCookie(profileCookie);

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

    // API: 유저 프로필 조회
    @GetMapping("/get-profile/{userId}")
    @Operation(summary = "유저 프로필 조회", description = "유저의 프로필을 반환합니다.")
    public BaseResponse<Object> getProfile(@PathVariable("userId") Long userId) {
        try{
            UserInfoResponseDto userProfile = userService.getProfile(userId);
            return new BaseResponse<>(userProfile);
        }catch (Exception e){
            return new BaseResponse<>(ResponseCode.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }
}
