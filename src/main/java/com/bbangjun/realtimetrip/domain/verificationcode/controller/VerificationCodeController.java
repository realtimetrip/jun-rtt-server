package com.bbangjun.realtimetrip.domain.verificationcode.controller;

import com.bbangjun.realtimetrip.domain.verificationcode.dto.SendVerificationCodeRequestDto;
import com.bbangjun.realtimetrip.domain.verificationcode.dto.SendVerificationCodeResponseDto;
import com.bbangjun.realtimetrip.domain.verificationcode.dto.VerifyEmailRequestDto;
import com.bbangjun.realtimetrip.domain.verificationcode.service.VerificationCodeService;
import com.bbangjun.realtimetrip.global.response.BaseResponse;
import com.bbangjun.realtimetrip.global.response.ResponseCode;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
@Tag(name = "VerificationCodeController", description = "인증 코드 관련 API")
public class VerificationCodeController {

    private final VerificationCodeService verificationCodeService;

    @Operation(summary = "DB 저장 방식 인증 번호 전송", description = "DB에 인증 번호를 저장하고, 이메일로 전송합니다.")
    @PostMapping("/send-db-verification-code")
    public BaseResponse<SendVerificationCodeResponseDto> sendDBVerificaitionCode(@RequestBody SendVerificationCodeRequestDto sendVerificationCodeRequestDto) {

        try{
            // 5분 이내에 다시 인증 번호 전송을 했다면 앞서 요청한 인증 번호 삭제
            verificationCodeService.deleteExistCode(sendVerificationCodeRequestDto.getEmail());
            verificationCodeService.sendEmailDB(sendVerificationCodeRequestDto.getEmail());
            return new BaseResponse<>(verificationCodeService.findByEmailDB(sendVerificationCodeRequestDto.getEmail()));
        }catch (Exception e) {
            return new BaseResponse<>(ResponseCode.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @Operation(summary = "DB 이메일 인증 번호 검증", description = "사용자로부터 입력 받은 인증 번호를 DB에 저장된 인증 번호로 검증합니다.")
    @PostMapping("verify-db-email")
    public BaseResponse<Object> verifyDbEmail(@RequestBody SendVerificationCodeResponseDto sendVerificationCodeResponseDto) {
        try{
            return verificationCodeService.checkVerificationCodeDB(sendVerificationCodeResponseDto.getEmail(), sendVerificationCodeResponseDto.getVerificationCode());
        }catch (Exception e){
            return new BaseResponse<>(ResponseCode.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @Operation(summary = "Redis 저장 방식 인증 번호 전송", description = "Redis에 인증 번호를 저장하고, 이메일로 전송합니다.")
    @PostMapping("/send-verification-code")
    public BaseResponse<SendVerificationCodeResponseDto> sendVerificationCode(@RequestBody SendVerificationCodeRequestDto sendVerificationCodeRequestDto) {

        try{
            // 5분 이내에 다시 인증 번호 전송을 했다면 앞서 요청한 인증 번호 삭제
            verificationCodeService.deleteExistCodeRedis(sendVerificationCodeRequestDto.getEmail());
            verificationCodeService.sendEmailRedis(sendVerificationCodeRequestDto.getEmail());
            return new BaseResponse<>(verificationCodeService.findByEmailRedis(sendVerificationCodeRequestDto.getEmail()));
        }catch (Exception e) {
            return new BaseResponse<>(ResponseCode.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @Operation(summary = "Redis 이메일 인증 번호 검증", description = "사용자로부터 입력 받은 인증 번호를 Redis에 저장된 인증 번호로 검증합니다.")
    @PostMapping("verify-email")
    public BaseResponse<Object> verifyEmail(@RequestBody VerifyEmailRequestDto verifyEmailRequestDto){
        try{
            return verificationCodeService.checkVerificationCodeRedis(verifyEmailRequestDto.getEmail(), verifyEmailRequestDto.getVerificationCode());
        }catch (Exception e){
            return new BaseResponse<>(ResponseCode.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }
}
