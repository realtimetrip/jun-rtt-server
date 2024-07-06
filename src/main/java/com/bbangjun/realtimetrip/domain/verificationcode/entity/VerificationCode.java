package com.bbangjun.realtimetrip.domain.verificationcode.entity;

import com.bbangjun.realtimetrip.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@Builder
@Table(name = "verification_code")
@NoArgsConstructor
@AllArgsConstructor
public class VerificationCode {

    @Id
    private String email;

    private String verificationCode;

    private LocalDateTime created_at;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;
}
