package com.bbangjun.realtimetrip.domain.chatmessage.repository;

import com.bbangjun.realtimetrip.domain.chatmessage.entity.ChatMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {

    @Query(value = "SELECT * FROM chat_message WHERE chat_room_id = :chatRoomId AND id < :chatId ORDER BY id DESC LIMIT :size", nativeQuery = true)
    List<ChatMessage> findTopMessagesByChatRoomIdAndChatId(@Param("chatRoomId") String chatRoomId, @Param("chatId") Long chatId, @Param("size") int size);
}
