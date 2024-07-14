package com.bbangjun.realtimetrip.domain.chatroom.service;

import com.bbangjun.realtimetrip.domain.chatmessage.dto.ChatMessageDto;
import com.bbangjun.realtimetrip.domain.chatmessage.dto.ChatMessagesResponseDto;
import com.bbangjun.realtimetrip.domain.chatmessage.entity.ChatMessage;
import com.bbangjun.realtimetrip.domain.chatmessage.repository.ChatMessageRepository;
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
    private final ChatMessageRepository chatMessageRepository;

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

    public ChatRoom findByChatRoomId(String roomId) {
        return chatRoomRepository.findByChatRoomId(roomId);
    }
}
