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

//    // name 필드: 현재 테이블에서 FK를 user_id로 설정
//    // referencedColumnName 필드: 참조하는 User 테이블의 PK 이름
//    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
//    @JoinColumn(name = "user_id", referencedColumnName = "userId")
//    private User user;
}
