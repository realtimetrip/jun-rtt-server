package com.bbangjun.realtimetrip.domain.chatmessage.dto;

import com.bbangjun.realtimetrip.domain.chatmessage.entity.ChatMessage;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChatMessagesResponseDto {
    private String chatRoomId;
    private String countryName;
    private List<ChatMessageDto> chatMessages = new ArrayList<>();
}
