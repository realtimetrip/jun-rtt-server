package com.bbangjun.realtimetrip.domain.chat.controller;

import com.bbangjun.realtimetrip.domain.chat.dto.ChatMessageDto;
import com.bbangjun.realtimetrip.domain.chat.entity.ChatMessage;
import com.bbangjun.realtimetrip.domain.chat.service.ChatService;
import com.bbangjun.realtimetrip.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;

@Slf4j
@RequiredArgsConstructor
@Controller
public class ChatController {

    // private final SimpMessageSendingOperations simpMessageSendingOperations;
    private final ChatService chatService;

    // @MessageMapping을 통해 메시지 핸들링 처리
    // 클라이언트에서 메시지 발행 시 /pub/chat/message 경로로 발행해야 하며, message에 RoomId를 지정해주어야 함.
    @MessageMapping("chat/message")
    public void message(ChatMessageDto chatMessageDto) {

        chatService.sendMessage(chatMessageDto);

        // convertAndSend는 수신한 메시지를 지정된 topic으로 broadcasting하는 기능을 수행함.(핸들링한 메시지를 지정된 topic으로 메시지 전달)
        //simpMessageSendingOperations.convertAndSend("/sub/chat/room/" + chatMessage.getRoomId(), chatMessage);
    }
}
