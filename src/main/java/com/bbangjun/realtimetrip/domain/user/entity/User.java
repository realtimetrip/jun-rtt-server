package com.bbangjun.realtimetrip.domain.user.entity;

import com.bbangjun.realtimetrip.domain.chatroomuser.ChatRoomUser;
import com.bbangjun.realtimetrip.domain.verificationcode.entity.VerificationCode;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Set;


@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    @NotNull
    @Column(unique = true)
    private String email;

    private String password;

    private String nickname;

    private String profile;

    // 현재 Entity가 PK, 연결되는 Entity에 선언한 현재 Entity 변수명을 mappedBy로 연결
    //
    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private VerificationCode verificationCode;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<ChatRoomUser> chatRoomUser;
}
