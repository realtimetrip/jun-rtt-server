package com.bbangjun.realtimetrip.domain.chat.entity;

import com.bbangjun.realtimetrip.domain.user.entity.User;
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
    @Enumerated(EnumType.STRING)
    private MessageType messageType;
    private String message;
    private LocalDateTime sendTime;

    // private Long userId;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_Id", referencedColumnName = "userId")
    private User user;

    // private String roomId;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "room_id", referencedColumnName = "roomId")
    private ChatRoom chatRoom;

    @Builder
    public ChatMessage(MessageType messageType, User user, ChatRoom chatRoom, String message, LocalDateTime sendTime){
        this.messageType = messageType;
        this.user = user;
        this.chatRoom = chatRoom;
        this.message = message;
        this.sendTime = sendTime;
    }
}
