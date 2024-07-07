package com.bbangjun.realtimetrip.domain.chat.entity;

import com.bbangjun.realtimetrip.domain.country.entity.Country;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@RequiredArgsConstructor
@Entity
@Getter
@Table(name = "chat_room")
public class ChatRoom {

    @Id
    private String chatRoomId;

    @OneToOne
    @JoinColumn(name = "country_code")
    private Country country;

    private Long userCount;

    @JsonManagedReference
    @OneToMany(mappedBy = "chatRoom", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<ChatMessage> chatMessages;

    @JsonManagedReference
    @OneToMany(mappedBy = "chatRoom", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<ChatRoomUser> chatRoomUsers;

    @Builder
    public ChatRoom(Country country){

        // 방 번호는 고유한 random UUID 생성
        this.chatRoomId = UUID.randomUUID().toString();
        this.country = country;
        this.userCount = 0L;
    }
}
