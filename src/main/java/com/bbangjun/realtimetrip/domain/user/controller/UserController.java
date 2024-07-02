package com.bbangjun.realtimetrip.domain.user.controller;

import com.bbangjun.realtimetrip.domain.user.dto.UserDto;
import com.bbangjun.realtimetrip.domain.user.service.UserService;
import com.bbangjun.realtimetrip.global.response.BaseResponse;
import com.bbangjun.realtimetrip.global.response.ResponseCode;
import com.bbangjun.realtimetrip.global.response.ResponseException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    // API: 회원가입
    @PostMapping("/signup")
    public BaseResponse<UserDto> signUp(@RequestBody UserDto userDto) {

        try{
            return new BaseResponse<>(userService.signUp(userDto));
        }catch (ResponseException e) {
            return new BaseResponse<>(e.getErrorCode(), e.getMessage());
        } catch (Exception e) {
            return new BaseResponse<>(ResponseCode.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    // API: 로그인
    @PostMapping("/login")
    public BaseResponse<?> login(@RequestBody UserDto userDto, HttpServletResponse response) {

        try{
            UserDto user = userService.authenticateUser(userDto.getEmail(), userDto.getPassword());

            if (user != null) {
                Cookie emailCookie = new Cookie("email", user.getEmail());
                Cookie nickNameCookie = new Cookie("nickName", user.getNickname());

                emailCookie.setMaxAge(7 * 24 * 60 * 60);
                nickNameCookie.setMaxAge(7 * 24 * 60 * 60);

                emailCookie.setPath("/");
                nickNameCookie.setPath("/");

                response.addCookie(emailCookie);
                response.addCookie(nickNameCookie);

                return new BaseResponse<>(user);
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
