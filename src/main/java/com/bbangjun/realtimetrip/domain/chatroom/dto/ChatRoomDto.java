package com.bbangjun.realtimetrip.domain.chatroom.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChatRoomDto implements Serializable {
    private String roomId;
    private String roomName;
    private Long count;
}
