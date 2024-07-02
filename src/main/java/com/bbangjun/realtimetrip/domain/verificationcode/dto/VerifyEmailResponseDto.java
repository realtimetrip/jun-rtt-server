package com.bbangjun.realtimetrip.domain.verificationcode.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VerifyEmailResponseDto {
    private String email;
    private String verificationCode;
}
