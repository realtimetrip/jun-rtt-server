package com.bbangjun.realtimetrip.domain.chat.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GetChatRoomResponseDto {
    private String chatRoomId;
    private String countryName;
    private Long userCount;
}
