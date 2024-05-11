package com.bbangjun.realtimetrip.domain.chat.controller;

import com.bbangjun.realtimetrip.domain.chat.dto.ChatMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Controller;

@Slf4j
@RequiredArgsConstructor
@Controller
public class ChatController {

    private final SimpMessageSendingOperations messagingTemplate;
    private final ChatRoomRepository  chatRoomRepository;

    // @MessageMapping을 통해 메시지 핸들링 처리
    // 클라이언트에서 메시지 발행 시 /pub/chat/message 경로로 발행해야 하며, message에 RoomId를 지정해주어야 함.
    @MessageMapping("chat/message")
    public void message(ChatMessage message) {

        // convertAndSend는 수신한 메시지를 지정된 topic으로 broadcasting하는 기능을 수행함.(핸들링한 메시지를 지정된 topic으로 메시지 전달)
        messagingTemplate.convertAndSend("/sub/chat/room/" + message.getRoomId(), message);
    }
}
