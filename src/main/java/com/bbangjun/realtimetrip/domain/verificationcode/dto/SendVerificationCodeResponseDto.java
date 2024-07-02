package com.bbangjun.realtimetrip.domain.verificationcode.dto;

import com.bbangjun.realtimetrip.domain.verificationcode.entity.VerificationCode;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SendVerificationCodeResponseDto {

    private String email;

    private String verificationCode;

    private LocalDateTime created_at;

    public static SendVerificationCodeResponseDto toVerificationCodeDto(VerificationCode verificationCode){
        SendVerificationCodeResponseDto sendVerificationCodeResponseDto = new SendVerificationCodeResponseDto();

        sendVerificationCodeResponseDto.setEmail(verificationCode.getEmail());
        sendVerificationCodeResponseDto.setVerificationCode(verificationCode.getVerificationCode());
        sendVerificationCodeResponseDto.setCreated_at(verificationCode.getCreated_at());

        return sendVerificationCodeResponseDto;
    }

    public SendVerificationCodeResponseDto(String email, String verificationCode) {
        this.email = email;
        this.verificationCode = verificationCode;
    }
}
