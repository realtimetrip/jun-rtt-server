package com.bbangjun.realtimetrip.domain.authnum.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class VerifyEmailDto {
    private String email;
    private String authNum;
}
