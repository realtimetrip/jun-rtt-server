package com.bbangjun.realtimetrip.domain.chat.dto;

import com.bbangjun.realtimetrip.domain.chat.entity.ChatMessage;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChatMessageDto {
    public enum MessageType{
        ENTER, TALK, LEAVE
    }

    private Long id;
    private MessageType messageType;
    private Long userId;
    private String nickName;
    private String roomId;
    private String message;
    private LocalDateTime sendTime;
}
