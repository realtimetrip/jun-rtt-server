package com.bbangjun.realtimetrip.domain.chatroom.controller;

import com.bbangjun.realtimetrip.domain.chat.dto.GetChatRoomResponseDto;
import com.bbangjun.realtimetrip.domain.chatroom.entity.ChatRoom;
import com.bbangjun.realtimetrip.domain.chatroom.service.ChatRoomService;
import com.bbangjun.realtimetrip.global.response.BaseResponse;
import com.bbangjun.realtimetrip.global.response.ResponseCode;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/chatroom")
@Tag(name = "ChatRoomController", description = "채팅방 관련 API")
public class ChatRoomController {

    private final ChatRoomService chatRoomService;

    // API: 채팅방 목록 조회
    @GetMapping("")
    @Operation(summary = "채팅방 목록 조회", description = "생성되어 있는 채팅방 목록을 조회합니다.")
    public BaseResponse<Object> getChatRoom() {

        try{
            List<GetChatRoomResponseDto> getChatRoomResponseDtoList = chatRoomService.getChatRoom();

            if(!getChatRoomResponseDtoList.isEmpty())
                return new BaseResponse<>(getChatRoomResponseDtoList);
            else
                return new BaseResponse<>(ResponseCode.NO_CHATROOM_EXIST);
        }catch (Exception e){
            return new BaseResponse<>(ResponseCode.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    // API: 채팅 내역 조회
    @GetMapping("/get-room")
    @Operation(summary = "채팅 내역 조회", description = "특정 채팅방의 채팅 내역을 조회합니다.")
    public ChatRoom roomInfo(@RequestParam("roomId") String roomId) {

        return chatRoomService.findByChatRoomId(roomId);
    }

    //    // 채팅방 생성
//    @PostMapping("/make-room")
//    @ResponseBody
//    public ChatRoom createRoom(@RequestParam("roomName") String roomName) {
//        ChatRoom newChatRoom = ChatRoom.builder()
//                .roomName(roomName).build();
//        return chatRoomRepository.save(newChatRoom);
//    }
}
