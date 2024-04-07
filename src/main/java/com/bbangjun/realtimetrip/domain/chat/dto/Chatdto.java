package com.bbangjun.realtimetrip.domain.chat.dto;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Chatdto {
    public enum MessageType{
        ENTER, TALK, LEAVE;
    }

    private MessageType type;
    private String roomId;
    private String sender;
    private String message;
    private String time;
}
