package com.bbangjun.realtimetrip.config.chat;

import com.bbangjun.realtimetrip.domain.chat.service.ChatService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

@Slf4j
@RequiredArgsConstructor
@Component
public class WebSocketEventListener {

    private final ChatService chatService;

    @EventListener
    public void handleWebSocketConnectListener(SessionConnectEvent event) {
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());
        String chatRoomId = (String) headerAccessor.getSessionAttributes().get("chatRoomId");
        Long userId = (Long) headerAccessor.getSessionAttributes().get("userId");

        if (chatRoomId != null && userId != null) {
            log.info("User Connected: " + userId + " to room: " + chatRoomId);
            //chatService.connectUser(chatRoomId, userId);
        }
    }

    @EventListener
    public void handleWebSocketDisconnectListener(SessionDisconnectEvent event) {
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());
        String chatRoomId = (String) headerAccessor.getSessionAttributes().get("chatRoomId");
        Long userId = (Long) headerAccessor.getSessionAttributes().get("userId");

        if (chatRoomId != null && userId != null) {
            log.info("User Disconnected: " + userId + " from room: " + chatRoomId);
            //chatService.disconnectUser(chatRoomId, userId);
        }
    }
}