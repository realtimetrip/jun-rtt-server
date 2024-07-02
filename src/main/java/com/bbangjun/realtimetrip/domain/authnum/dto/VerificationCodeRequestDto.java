package com.bbangjun.realtimetrip.domain.authnum.dto;

import com.bbangjun.realtimetrip.domain.authnum.entity.VerificationCode;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VerificationCodeRequestDto {

    private String email;

    private String verificationCode;

    private LocalDateTime created_at;

    public static VerificationCodeRequestDto toVerificationCodeDto(VerificationCode verificationCode){
        VerificationCodeRequestDto verificationCodeRequestDto = new VerificationCodeRequestDto();

        verificationCodeRequestDto.setEmail(verificationCode.getEmail());
        verificationCodeRequestDto.setVerificationCode(verificationCode.getVerificationCode());
        verificationCodeRequestDto.setCreated_at(verificationCode.getCreated_at());

        return verificationCodeRequestDto;
    }

    public VerificationCodeRequestDto(String email, String authNum) {
        this.email = email;
        this.verificationCode = authNum;
    }
}
