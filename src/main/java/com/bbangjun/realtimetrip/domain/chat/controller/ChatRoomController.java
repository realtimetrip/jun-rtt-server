package com.bbangjun.realtimetrip.domain.chat.controller;

import com.bbangjun.realtimetrip.domain.chat.entity.ChatRoom;
import com.bbangjun.realtimetrip.domain.chat.repository.ChatRoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/chatroom")
public class ChatRoomController {

    private final ChatRoomRepository chatRoomRepository;

    // API: 채팅방 목록 조회
    @GetMapping("")
    @ResponseBody
    public List<ChatRoom> getChatRoom() {
        return chatRoomRepository.findAll();
    }

    // 채팅방 생성
    @PostMapping("/make-room")
    @ResponseBody
    public ChatRoom createRoom(@RequestParam("roomName") String roomName) {
        ChatRoom newChatRoom = ChatRoom.builder()
                .roomName(roomName).build();
        return chatRoomRepository.save(newChatRoom);
    }


    // 특정 채팅방 조회
    @GetMapping("/get-room")
    @ResponseBody
    public ChatRoom roomInfo(@RequestParam("roomId") String roomId) {
        return chatRoomRepository.findByChatRoomId(roomId);
    }
}
