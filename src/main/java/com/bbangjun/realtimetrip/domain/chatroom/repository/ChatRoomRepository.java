package com.bbangjun.realtimetrip.domain.chatroom.repository;

import com.bbangjun.realtimetrip.domain.chatroom.entity.ChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long>{

    ChatRoom findByChatRoomId(String chatRoomId);
}