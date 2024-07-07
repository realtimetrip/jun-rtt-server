package com.bbangjun.realtimetrip.domain.chat.entity;

import com.bbangjun.realtimetrip.domain.user.entity.User;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@RequiredArgsConstructor
@Getter
@Entity
@Table(name = "chat_message")
public class ChatMessage {

    public enum MessageType{
        ENTER, TALK, LEAVE;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonBackReference
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "chatRoomId", nullable = false)
    private ChatRoom chatRoom;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userId", nullable = false)
    private User user;

    private LocalDateTime eventTime;

    private String message;

    @Enumerated(EnumType.STRING)
    private MessageType type;

    private Long chatId; // 채팅방 내의 시퀀스 ID

    @Builder
    public ChatMessage(MessageType type, User user, ChatRoom chatRoom, String message, LocalDateTime eventTime, Long chatId){
        this.type = type;
        this.user = user;
        this.chatRoom = chatRoom;
        this.message = message;
        this.eventTime = eventTime;
        this.chatId = chatId;
    }
}
