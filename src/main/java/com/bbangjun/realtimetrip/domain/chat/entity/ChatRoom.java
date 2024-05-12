package com.bbangjun.realtimetrip.domain.chat.entity;

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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true)
    private String roomId;
    private String roomName;
    private Long count;

    @OneToMany(mappedBy = "chatRoom", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<ChatMessage> messages = new HashSet<>();

    @Builder
    public ChatRoom(String roomName){

        // 방 번호는 고유한 random UUID 생성
        this.roomId = UUID.randomUUID().toString();
        this.roomName = roomName;
        this.count = 0L;
    }
}
