package com.bbangjun.realtimetrip.domain.chatroom.entity;

import com.bbangjun.realtimetrip.domain.chatmessage.entity.ChatMessage;
import com.bbangjun.realtimetrip.domain.chatroomuser.ChatRoomUser;
import com.bbangjun.realtimetrip.domain.country.entity.Country;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

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

    @Version
    private Long lastChatId; // 메시지 시퀀스 관리

    @Builder
    public ChatRoom(Country country){

        // 방 번호는 고유한 random UUID 생성
        this.chatRoomId = UUID.randomUUID().toString();
        this.country = country;
        this.userCount = 0L;
        this.lastChatId = 0L;  // 초기 시퀀스 값
    }

    public void setLastChatId(Long lastChatId) {
        this.lastChatId = lastChatId;
    }

    public void increaseUserCount(){
        this.userCount++;
    }
}
