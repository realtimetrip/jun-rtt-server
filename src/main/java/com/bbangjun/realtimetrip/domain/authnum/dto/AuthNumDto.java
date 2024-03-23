package com.bbangjun.realtimetrip.domain.authnum.dto;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuthNumDto {

    private String email;

    private String authNum;

    private LocalDateTime created_at;
}
