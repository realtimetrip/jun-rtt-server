package com.bbangjun.realtimetrip.domain.chat.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChatRoomDto {
    private String roomId;
    private String roomName;
    private Long count;
}
