package com.bbangjun.realtimetrip.domain.authnum.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuthNum {

    @Id
    private String email;

    private String authNum;

    private LocalDateTime created_at;
}
