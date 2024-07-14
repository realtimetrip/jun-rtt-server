package com.bbangjun.realtimetrip.domain.chatroom.service;

import com.bbangjun.realtimetrip.domain.chatroom.dto.GetChatRoomResponseDto;
import com.bbangjun.realtimetrip.domain.chatroom.entity.ChatRoom;
import com.bbangjun.realtimetrip.domain.chatroom.repository.ChatRoomRepository;
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

    public List<GetChatRoomResponseDto> getChatRoom() {

        List<ChatRoom> chatRoomList = chatRoomRepository.findAll();
        List<GetChatRoomResponseDto> getChatRoomResponseDtoList = new ArrayList<>();

        for(ChatRoom chatRoom : chatRoomList){
            GetChatRoomResponseDto getChatRoomResponseDto = new GetChatRoomResponseDto();
            getChatRoomResponseDto.setChatRoomId(chatRoom.getChatRoomId());
            getChatRoomResponseDto.setCountryName(chatRoom.getCountry().getCountryName());
            getChatRoomResponseDto.setUserCount(chatRoom.getUserCount());

            getChatRoomResponseDtoList.add(getChatRoomResponseDto);
        }

        return getChatRoomResponseDtoList;
    }

    public ChatRoom findByChatRoomId(String roomId) {
        return chatRoomRepository.findByChatRoomId(roomId);
    }
}
