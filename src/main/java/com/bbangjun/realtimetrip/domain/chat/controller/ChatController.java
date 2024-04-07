package com.bbangjun.realtimetrip.domain.chat.controller;

import com.bbangjun.realtimetrip.domain.chat.dto.Chatdto;
import com.bbangjun.realtimetrip.domain.chat.repository.ChatRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RestController
public class ChatController {

    private final SimpMessageSendingOperations template;
    private final ChatRepository repository;

    @MessageMapping("/enterUser")
    public void enterUser(@Payload Chatdto chat, SimpMessageHeaderAccessor headerAccessor) {
        repository.plusUserCnt(chat.getRoomId());
        String userUUID = repository.addUser(chat.getRoomId(), chat.getSender());

        headerAccessor.getSessionAttributes().put("userUUID", userUUID);
        headerAccessor.getSessionAttributes().put("roomId", chat.getRoomId());

        chat.setMessage(chat.getSender() + "님 입장!!");
        template.convertAndSend("/sub/chat/room/" + chat.getRoomId(), chat);
    }

    @MessageMapping("/sendMessage")
    public void sendMessage(@Payload Chatdto chat){
        log.info("CHAT {}", chat);
        chat.setMessage(chat.getMessage());
        template.convertAndSend("/sub/chat/room/" + chat.getRoomId(), chat);

    }
}
