package com.bbangjun.realtimetrip.domain.chat.entity;

import com.bbangjun.realtimetrip.domain.country.entity.Country;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.listener.ChannelTopic;

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

    private String roomName;

    private Long userCount;

    @OneToMany(mappedBy = "chatRoom", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<ChatMessage> messages = new HashSet<>();

    @OneToOne
    @JoinColumn(name = "country_code")
    private Country country;

    @Builder
    public ChatRoom(String roomName){

        // 방 번호는 고유한 random UUID 생성
        this.chatRoomId = UUID.randomUUID().toString();
        this.roomName = roomName;
        this.userCount = 0L;
    }
}
