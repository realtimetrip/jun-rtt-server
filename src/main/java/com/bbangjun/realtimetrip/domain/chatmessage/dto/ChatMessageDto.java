package com.bbangjun.realtimetrip.domain.chatmessage.dto;

import com.bbangjun.realtimetrip.domain.chatmessage.entity.ChatMessage;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChatMessageDto {
    private Long chatId;
    private Long userId;
    private String nickName;
    private String profile;
    private String message;
    private String eventTime;

    private static String formatDateTime(LocalDateTime dateTime) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        return dateTime.format(formatter);
    }

    public static ChatMessageDto toChatMessageDto(ChatMessage chatMessage){
        ChatMessageDto chatMessageDto = new ChatMessageDto();
        chatMessageDto.chatId = chatMessage.getChatId();
        chatMessageDto.userId = chatMessage.getUser().getUserId();
        chatMessageDto.nickName = chatMessage.getUser().getNickname();
        chatMessageDto.profile = chatMessage.getUser().getProfile();
        chatMessageDto.message = chatMessage.getMessage();
        chatMessageDto.eventTime = formatDateTime(chatMessage.getEventTime());

        return chatMessageDto;
    }
}
