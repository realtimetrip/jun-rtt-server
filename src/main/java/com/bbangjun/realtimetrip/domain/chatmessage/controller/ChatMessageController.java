package com.bbangjun.realtimetrip.domain.chatmessage.controller;

import com.bbangjun.realtimetrip.domain.chatmessage.dto.*;
import com.bbangjun.realtimetrip.domain.chatmessage.service.ChatMessageService;
import com.bbangjun.realtimetrip.global.response.BaseResponse;
import com.bbangjun.realtimetrip.global.response.ResponseCode;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
public class ChatMessageController {

    private final ChatMessageService chatMessageService;

    // API: 채팅 보내기
    // websocket "pub/send-message"로 들어오는 메시징을 처리
    @MessageMapping("/send-message")
    public BaseResponse<Object> sendMessage(SendMessageRequestDto sendMessageRequestDto){
        try{
            log.info("send message = {}", sendMessageRequestDto);
            SendMessageResponseDto sendMessageResponseDto = chatMessageService.sendMessage(sendMessageRequestDto);
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
            log.info("enter user = {}", enterUserRequestDto);
            EnterUserResponseDto enterUserResponseDto = chatMessageService.enterUser(enterUserRequestDto);
            if(enterUserResponseDto != null){
                return new BaseResponse<>(enterUserResponseDto);
            }
            else{
                return new BaseResponse<>(ResponseCode.NO_ENTER_TYPE);
            }
        }catch(Exception e){
            log.info("error = {}", e.getMessage());
            return new BaseResponse<>(ResponseCode.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }
}
