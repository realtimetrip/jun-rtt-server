package com.bbangjun.realtimetrip.domain.chatroomuser;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChatRoomUserRepository extends JpaRepository<ChatRoomUser, Long> {

    @Query(value = "SELECT * FROM chat_room_user WHERE chat_room_id = :chatRoomId", nativeQuery = true)
    List<ChatRoomUser> findByChatRoomId(@Param("chatRoomId") String chatRoomId);
}
