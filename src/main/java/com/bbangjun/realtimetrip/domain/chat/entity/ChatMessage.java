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
    private Long chatId;

    // chat_room_id
    // private String roomId;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "chatRoomId", referencedColumnName = "chatRoomId")
    private ChatRoom chatRoom;

    // user_id, nickname
    // private Long userId;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userId", referencedColumnName = "userId")
    private User user;

    // event_time
    private LocalDateTime eventTime;

    // message
    private String message;

    @Enumerated(EnumType.STRING)
    private MessageType type;

    @Builder
    public ChatMessage(MessageType type, User user, ChatRoom chatRoom, String message, LocalDateTime eventTime){
        this.type = type;
        this.user = user;
        this.chatRoom = chatRoom;
        this.message = message;
        this.eventTime = eventTime;
    }
}
