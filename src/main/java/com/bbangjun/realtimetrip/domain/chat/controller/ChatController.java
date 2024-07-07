package com.bbangjun.realtimetrip.domain.chat.controller;

import com.bbangjun.realtimetrip.domain.chat.dto.EnterUserResponseDto;
import com.bbangjun.realtimetrip.domain.chat.dto.EnterUserRequestDto;
import com.bbangjun.realtimetrip.domain.chat.dto.SendMessageRequestDto;
import com.bbangjun.realtimetrip.domain.chat.dto.SendMessageResponseDto;
import com.bbangjun.realtimetrip.domain.chat.service.ChatService;
import com.bbangjun.realtimetrip.global.response.BaseResponse;
import com.bbangjun.realtimetrip.global.response.ResponseCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;

@Slf4j
@RequiredArgsConstructor
@Controller
public class ChatController {

    private final ChatService chatService;

    // API: 채팅 보내기
    // websocket "pub/send-message"로 들어오는 메시징을 처리
    @MessageMapping("/send-message")
    public BaseResponse<Object> sendMessage(SendMessageRequestDto sendMessageRequestDto){
        try{
            SendMessageResponseDto sendMessageResponseDto = chatService.sendMessage(sendMessageRequestDto);
            if(sendMessageResponseDto != null){
                return new BaseResponse<>(sendMessageResponseDto);
            }
            else{
                return new BaseResponse<>(ResponseCode.NO_TALK_TYPE);
            }
        }catch (Exception e){
            return new BaseResponse<>(ResponseCode.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    // API: 채팅방 입장
    // websocket "/pub/enter-user"로 들어오는 메시징을 처리
    @MessageMapping("/enter-user")
    public BaseResponse<Object> enterUser(EnterUserRequestDto enterUserRequestDto) {
        try{
            EnterUserResponseDto enterUserResponseDto = chatService.enterUser(enterUserRequestDto);
            if(enterUserResponseDto != null){
                return new BaseResponse<>(enterUserResponseDto);
            }
            else{
                return new BaseResponse<>(ResponseCode.NO_ENTER_TYPE);
            }
        }catch(Exception e){
            return new BaseResponse<>(ResponseCode.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

}
