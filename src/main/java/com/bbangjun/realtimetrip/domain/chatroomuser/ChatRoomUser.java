package com.bbangjun.realtimetrip.domain.chatroomuser;

import com.bbangjun.realtimetrip.domain.chatroom.entity.ChatRoom;
import com.bbangjun.realtimetrip.domain.user.entity.User;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Builder;
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

    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "chat_room_id", nullable = false)
    private ChatRoom chatRoom;

    @Builder
    public ChatRoomUser(User user, ChatRoom chatRoom){
        this.chatRoomUserId = user.getUserId();
        this.user = user;
        this.chatRoom = chatRoom;
    }
}
