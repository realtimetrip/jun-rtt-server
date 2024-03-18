package com.bbangjun.realtimetrip.authnum.entity;

import com.bbangjun.realtimetrip.user.entity.User;
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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long authNum;

    private LocalDateTime created_at;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)

    // name 필드: 현재 테이블에서 FK를 user_id로 설정
    // referencedColumnName 필드: 참조하는 User 테이블의 PK 이름
    @JoinColumn(name = "user_id", referencedColumnName = "userId")
    private User user;
}
