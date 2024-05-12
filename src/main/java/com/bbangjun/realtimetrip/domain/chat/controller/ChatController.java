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


    // @MessageMapping을 통해 메시지 핸들링 처리
    // 클라이언트에서 메시지 발행 시 /pub/chat/message 경로로 발행해야 하며, message에 RoomId를 지정해주어야 함.

//    @MessageMapping("/enterUser")
//    public void enterUser(@Payload ChatMessageDto chatMessageDto, SimpMessageHeaderAccessor headerAccessor) throws JsonProcessingException {
//
//        log.info("chatMessageDto = {}", chatMessageDto);
//        chatService.enterUser(chatMessageDto, headerAccessor);
//    }



    /**
     * websocket "/pub/chat/message"로 들어오는 메시징을 처리한다.
     */
    @MessageMapping("/chat/message")
    public void enterUser(ChatMessageDto chatMessageDto) {
        chatService.enterUser(chatMessageDto);
    }
}
