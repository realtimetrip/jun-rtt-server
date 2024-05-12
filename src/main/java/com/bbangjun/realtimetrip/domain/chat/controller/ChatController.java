package com.bbangjun.realtimetrip.domain.chat.controller;

import com.bbangjun.realtimetrip.config.RedisPublisher;
import com.bbangjun.realtimetrip.domain.chat.dto.ChatMessageDto;
import com.bbangjun.realtimetrip.domain.chat.entity.ChatMessage;
import com.bbangjun.realtimetrip.domain.chat.service.ChatService;
import com.bbangjun.realtimetrip.domain.user.repository.UserRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;

@Slf4j
@RequiredArgsConstructor
@Controller
public class ChatController {

    private final ChatService chatService;

    @MessageMapping("/chat/send")
    public void sendMessage(ChatMessageDto chatMessageDto){
        chatService.sendMessage(chatMessageDto);
    }

    // websocket "/pub/chat/enter"로 들어오는 메시징을 처리
    @MessageMapping("/chat/enter")
    public void enterUser(ChatMessageDto chatMessageDto) {
        chatService.enterUser(chatMessageDto);
    }
}
