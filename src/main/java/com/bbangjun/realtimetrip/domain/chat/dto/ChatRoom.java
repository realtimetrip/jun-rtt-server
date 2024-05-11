package com.bbangjun.realtimetrip.domain.chat.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.UUID;

@Data
public class ChatRoom {
    private String roomId;
    private String roomName;
    private Long count;

    public static ChatRoom create(String roomName){
        ChatRoom chatRoom = new ChatRoom();
        // 방 번호는 고유한 random UUID 생성
        chatRoom.roomId = UUID.randomUUID().toString();
        chatRoom.roomName = roomName;
        chatRoom.count = 0L;

        return chatRoom;
    }
}
