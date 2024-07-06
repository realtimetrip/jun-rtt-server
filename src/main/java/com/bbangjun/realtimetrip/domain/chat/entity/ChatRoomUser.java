package com.bbangjun.realtimetrip.domain.chat.entity;

import com.bbangjun.realtimetrip.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Entity
@Getter
@Table(name = "chat_room_user")
public class ChatRoomUser {

    @Id
    private Long chatRoomUserId;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "chat_room_id", nullable = false)
    private ChatRoom chatRoom;
}
