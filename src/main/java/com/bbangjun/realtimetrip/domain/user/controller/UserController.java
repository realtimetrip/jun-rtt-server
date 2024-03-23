package com.bbangjun.realtimetrip.domain.user.controller;

import com.bbangjun.realtimetrip.domain.authnum.service.AuthNumService;
import com.bbangjun.realtimetrip.domain.user.dto.UserDto;
import com.bbangjun.realtimetrip.domain.user.service.UserService;
import com.bbangjun.realtimetrip.global.response.BaseResponse;
import com.bbangjun.realtimetrip.global.response.ResponseCode;
import com.bbangjun.realtimetrip.global.response.ResponseException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {

    private final UserService userService;
    private final AuthNumService authNumService;

    // 회원가입
    @PostMapping("/signup")
    public BaseResponse<UserDto> signUp(@RequestBody UserDto userDto, @RequestParam(name = "authNum") String authNum) {

        try{
            if(authNumService.checkAuthNum(userDto.getEmail(), authNum))
                return new BaseResponse<>(userService.signUp(userDto));
            else
                return new BaseResponse<>(ResponseCode.INCORRECT_AUTHCODE);
        }catch (ResponseException e) {
            return new BaseResponse<>(e.getErrorCode(), e.getMessage());
        } catch (Exception e) {
            return new BaseResponse<>(ResponseCode.INTERNAL_SERVER_ERROR, e.getMessage());
        }


    }
}
