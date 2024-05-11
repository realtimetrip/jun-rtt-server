package com.bbangjun.realtimetrip.domain.chat.dto;

import lombok.*;

@Data
@Builder
public class ChatMessage {
    public enum MessageType{
        ENTER, TALK, LEAVE;
    }

    private MessageType type;
    private String roomId;
    private String sender;
    private String message;
    private String time;
}
