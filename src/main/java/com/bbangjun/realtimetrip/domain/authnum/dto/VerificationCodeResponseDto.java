package com.bbangjun.realtimetrip.domain.authnum.dto;

import com.bbangjun.realtimetrip.domain.authnum.entity.VerificationCode;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VerificationCodeResponseDto {

    private String email;

    private String verificationCode;

    private LocalDateTime created_at;

    public static VerificationCodeResponseDto toVerificationCodeDto(VerificationCode verificationCode){
        VerificationCodeResponseDto verificationCodeResponseDto = new VerificationCodeResponseDto();

        verificationCodeResponseDto.setEmail(verificationCode.getEmail());
        verificationCodeResponseDto.setVerificationCode(verificationCode.getVerificationCode());
        verificationCodeResponseDto.setCreated_at(verificationCode.getCreated_at());

        return verificationCodeResponseDto;
    }

    public VerificationCodeResponseDto(String email, String authNum) {
        this.email = email;
        this.verificationCode = authNum;
    }
}
