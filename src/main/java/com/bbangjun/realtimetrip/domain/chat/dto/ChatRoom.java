package com.bbangjun.realtimetrip.domain.chat.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.UUID;

@Getter
@Setter
public class ChatRoom {
    private String roomId;
    private String roomName;
    private long userCount;
    private HashMap<String, String> userList = new HashMap<String, String>();

    public ChatRoom create(String roomName){
        ChatRoom chatRoom = new ChatRoom();
        chatRoom.roomId = UUID.randomUUID().toString();
        chatRoom.roomName = roomName;

        return chatRoom;
    }
}
