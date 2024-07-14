package com.bbangjun.realtimetrip.domain.chatmessage.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SendMessageRequestDto {
    public enum MessageType{
        ENTER, TALK, LEAVE
    }

    private String chatRoomId;
    private Long userId;
    private MessageType type;
    private String message;
}
