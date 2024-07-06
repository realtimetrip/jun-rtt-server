package com.bbangjun.realtimetrip.domain.chat.controller;

import com.bbangjun.realtimetrip.domain.chat.dto.ChatMessageDto;
import com.bbangjun.realtimetrip.domain.chat.service.ChatService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Slf4j
@RequiredArgsConstructor
@Controller
public class ChatController {

    private final ChatService chatService;

    // API: 채팅 보내기
    // websocket "pub/send-message"로 들어오는 메시징을 처리
    @MessageMapping("/send-message")
    public void sendMessage(ChatMessageDto chatMessageDto){
        chatService.sendMessage(chatMessageDto);
    }

    // API: 채팅방 입장
    // websocket "/pub/enter-user"로 들어오는 메시징을 처리
    @MessageMapping("/enter-user")
    public void enterUser(ChatMessageDto chatMessageDto) {
        chatService.enterUser(chatMessageDto);
    }

}
