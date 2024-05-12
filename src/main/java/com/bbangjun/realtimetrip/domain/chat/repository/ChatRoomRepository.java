package com.bbangjun.realtimetrip.domain.chat.repository;

import com.bbangjun.realtimetrip.domain.chat.entity.ChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long>{

    ChatRoom findByRoomId(String roomId);

    // List<ChatRoom> findAllChatRoom();
}