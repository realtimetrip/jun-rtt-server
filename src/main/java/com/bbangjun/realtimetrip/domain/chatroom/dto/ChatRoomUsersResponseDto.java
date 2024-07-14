package com.bbangjun.realtimetrip.domain.chatroom.dto;

import com.bbangjun.realtimetrip.domain.chatroomuser.ChatRoomUser;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChatRoomUsersResponseDto {
    private Long userId;
    private String nickName;
    private String profile;

    public static ChatRoomUsersResponseDto toChatRoomUserDto(ChatRoomUser chatRoomUser) {
        ChatRoomUsersResponseDto chatRoomUsersResponseDto = new ChatRoomUsersResponseDto();
        chatRoomUsersResponseDto.userId = chatRoomUser.getChatRoomUserId();
        chatRoomUsersResponseDto.nickName = chatRoomUser.getUser().getNickname();
        chatRoomUsersResponseDto.profile = chatRoomUser.getUser().getProfile();

        return chatRoomUsersResponseDto;
    }
}
