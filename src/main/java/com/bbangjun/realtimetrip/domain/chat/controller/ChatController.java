package com.bbangjun.realtimetrip.domain.chat.controller;

import com.bbangjun.realtimetrip.domain.chat.dto.ChatMessageDto;
import com.bbangjun.realtimetrip.domain.chat.service.ChatService;
import com.bbangjun.realtimetrip.global.response.BaseResponse;
import com.bbangjun.realtimetrip.global.response.ResponseCode;
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
    public BaseResponse<Object> enterUser(ChatMessageDto chatMessageDto) {
        try{
            ChatMessageDto chatMessage = chatService.enterUser(chatMessageDto);
            if(chatMessage != null){
                return new BaseResponse<>(chatMessage);
            }
            else{
                return new BaseResponse<>(ResponseCode.NO_ENTER_TYPE);
            }
        }catch(Exception e){
            return new BaseResponse<>(ResponseCode.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

}
