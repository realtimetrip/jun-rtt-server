package com.bbangjun.realtimetrip.domain.chat.repository;

import com.bbangjun.realtimetrip.domain.chat.dto.ChatRoom;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
@Slf4j
public class ChatRepository {

    private Map<String, ChatRoom> chatRoomMap = new LinkedHashMap<>();

    // 전체 채팅방 조회
    public List<ChatRoom> findAllRoom(){
        List chatRooms = new ArrayList<>(chatRoomMap.values());
        Collections.reverse(chatRooms);

        return chatRooms;
    }

    // roomId 기준으로 채팅방 찾기
    public ChatRoom findRoomById(String roomId){
        return chatRoomMap.get(roomId);
    }

    // roomName으로 채팅방 만들기
    public ChatRoom createChatRoom(String roomName){
        ChatRoom chatRoom = new ChatRoom().create(roomName);
        chatRoomMap.put(chatRoom.getRoomId(), chatRoom);

        return chatRoom;
    }

    // 채팅방 인원+1
    public void plusUserCnt(String roomId){
        ChatRoom room = chatRoomMap.get(roomId);
        room.setUserCount(room.getUserCount()+1);
    }

    // 채팅방 인원-1
    public void minusUserCnt(String roomId){
        ChatRoom room = chatRoomMap.get(roomId);
        room.setUserCount(room.getUserCount()-1);
    }

    // 채팅방 유저 리스트에 유저 추가
    public String addUser(String roomId, String userName){
        ChatRoom room = chatRoomMap.get(roomId);
        String userUUID = UUID.randomUUID().toString();

        room.getUserList().put(userUUID, userName);

        return userUUID;
    }

    // 채팅방 유저 리스트 삭제
    public void delUser(String roomId, String userUUID){
        ChatRoom room = chatRoomMap.get(roomId);
        room.getUserList().remove(userUUID);
    }

    // 채팅방 userName 조회
    public String getUserName(String roomId, String userUUID){
        ChatRoom room = chatRoomMap.get(roomId);
        return room.getUserList().get(userUUID);
    }

    // 채팅방 전체 userlist 조회
    public ArrayList<String> getUserList(String  roomId){
        ArrayList<String> list = new ArrayList<>();
        ChatRoom room = chatRoomMap.get(roomId);

        room.getUserList().forEach((key, value) -> list.add(value));
        return list;
    }
}
