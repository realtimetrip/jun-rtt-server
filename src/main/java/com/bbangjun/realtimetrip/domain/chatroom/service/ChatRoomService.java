package com.bbangjun.realtimetrip.domain.chatroom.service;

import com.bbangjun.realtimetrip.domain.chatmessage.dto.ChatMessageDto;
import com.bbangjun.realtimetrip.domain.chatmessage.dto.ChatMessagesResponseDto;
import com.bbangjun.realtimetrip.domain.chatmessage.entity.ChatMessage;
import com.bbangjun.realtimetrip.domain.chatmessage.repository.ChatMessageRepository;
import com.bbangjun.realtimetrip.domain.chatroom.dto.ChatRoomResponseDto;
import com.bbangjun.realtimetrip.domain.chatroom.dto.ChatRoomUsersResponseDto;
import com.bbangjun.realtimetrip.domain.chatroom.entity.ChatRoom;
import com.bbangjun.realtimetrip.domain.chatroom.repository.ChatRoomRepository;
import com.bbangjun.realtimetrip.domain.chatroomuser.ChatRoomUser;
import com.bbangjun.realtimetrip.domain.chatroomuser.ChatRoomUserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ChatRoomService {

    private final ChatRoomRepository chatRoomRepository;
    private final ChatMessageRepository chatMessageRepository;
    private final ChatRoomUserRepository chatRoomUserRepository;

    public List<ChatRoomResponseDto> getChatRoom() {

        List<ChatRoom> chatRoomList = chatRoomRepository.findAll();
        List<ChatRoomResponseDto> chatRoomResponseDtoList = new ArrayList<>();

        for(ChatRoom chatRoom : chatRoomList){
            ChatRoomResponseDto chatRoomResponseDto = new ChatRoomResponseDto();
            chatRoomResponseDto.setChatRoomId(chatRoom.getChatRoomId());
            chatRoomResponseDto.setCountryName(chatRoom.getCountry().getCountryName());
            chatRoomResponseDto.setUserCount(chatRoom.getUserCount());

            chatRoomResponseDtoList.add(chatRoomResponseDto);
        }

        return chatRoomResponseDtoList;
    }

    public ChatMessagesResponseDto getChatMessages(String chatRoomId, Long size, Long chatId) {

        List<ChatMessage> chatMessageList = chatMessageRepository.findTopMessagesByChatRoomIdAndChatId(chatRoomId, chatId, size.intValue());

        ChatMessagesResponseDto chatMessagesResponseDto = new ChatMessagesResponseDto();
        chatMessagesResponseDto.setChatRoomId(chatRoomId);
        chatMessagesResponseDto.setCountryName(chatRoomRepository.findByChatRoomId(chatRoomId).getCountry().getCountryName());
        // DTO로 변환
        for(ChatMessage chatMessage : chatMessageList){
            chatMessagesResponseDto.getChatMessages().add(ChatMessageDto.toChatMessageDto(chatMessage));
        }

        return chatMessagesResponseDto;
    }

    public List<ChatRoomUsersResponseDto> getChatRoomUsers(String chatRoomId) {

        List<ChatRoomUser> chatRoomUserList = chatRoomUserRepository.findByChatRoomId(chatRoomId);

        List<ChatRoomUsersResponseDto> chatRoomUsersResponseDtoList = new ArrayList<>();

        for(ChatRoomUser chatRoomUser : chatRoomUserList){
            chatRoomUsersResponseDtoList.add(ChatRoomUsersResponseDto.toChatRoomUserDto(chatRoomUser));
        }

        return chatRoomUsersResponseDtoList;
    }
}
