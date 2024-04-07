package com.bbangjun.realtimetrip.domain.chat.controller;

import com.bbangjun.realtimetrip.domain.chat.dto.ChatRoom;
import com.bbangjun.realtimetrip.domain.chat.repository.ChatRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/chatroom")
public class ChatRoomController {
    private final ChatRepository chatRepository;

    // 채팅 리스트 화면
    @GetMapping("/")
    public ResponseEntity<?> goChatRoom(){
        List<ChatRoom> chatRooms = chatRepository.findAllRoom();
        return ResponseEntity.ok(chatRooms);
    }

    // 채팅방 생성
    @PostMapping("/room")
    public ResponseEntity<?> createRoom(@RequestParam(name = "name") String name){
        ChatRoom room = chatRepository.createChatRoom(name);
        return ResponseEntity.ok(room.getRoomId());
    }

    // 채팅에 참여한 유저 리스트 반환
    @GetMapping("/userlist")
    public ArrayList<String> userList(String roomId){
        return chatRepository.getUserList(roomId);
    }
}
