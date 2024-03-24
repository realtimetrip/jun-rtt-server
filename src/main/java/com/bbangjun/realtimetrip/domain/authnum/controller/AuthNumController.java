package com.bbangjun.realtimetrip.domain.authnum.controller;

import com.bbangjun.realtimetrip.domain.authnum.dto.AuthNumDto;
import com.bbangjun.realtimetrip.domain.authnum.service.AuthNumService;
import com.bbangjun.realtimetrip.domain.user.dto.UserDto;
import com.bbangjun.realtimetrip.global.response.BaseResponse;
import com.bbangjun.realtimetrip.global.response.ResponseCode;
import com.bbangjun.realtimetrip.global.response.ResponseException;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/authnum")
public class AuthNumController {

    private final AuthNumService authNumService;

    // 인증 번호 전송
    @PostMapping("/send")
    public BaseResponse<AuthNumDto> sendAuthNum(@RequestBody UserDto userDto) throws MessagingException, UnsupportedEncodingException {

        try{
            // 5분 이내에 다시 인증 번호 전송을 했다면 앞서 요청한 인증 번호 삭제
            authNumService.deleteExistCode(userDto.getEmail());
            authNumService.sendEmail(userDto.getEmail());
            return new BaseResponse<>(authNumService.findByEmail(userDto.getEmail()));
        }catch (Exception e) {
            return new BaseResponse<>(ResponseCode.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }
}
