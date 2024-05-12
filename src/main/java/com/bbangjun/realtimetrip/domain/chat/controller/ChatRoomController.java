package com.bbangjun.realtimetrip.domain.chat.controller;

import com.bbangjun.realtimetrip.domain.chat.entity.ChatRoom;
import com.bbangjun.realtimetrip.domain.chat.repository.ChatRoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/chat")
public class ChatRoomController {

    private final ChatRoomRepository chatRoomRepository;

    // 모든 채팅방 목록 반환
    @GetMapping("/get-rooms")
    @ResponseBody
    public List<ChatRoom> room() {
        List<ChatRoom> list = chatRoomRepository.findAll();
        return list;
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
        return chatRoomRepository.findByRoomId(roomId);
    }
}
