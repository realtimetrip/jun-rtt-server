package com.bbangjun.realtimetrip.domain.chat.service;

import com.bbangjun.realtimetrip.domain.chat.dto.ChatMessageDto;
import com.bbangjun.realtimetrip.domain.chat.entity.ChatMessage;
import com.bbangjun.realtimetrip.domain.chat.entity.ChatRoom;
import com.bbangjun.realtimetrip.domain.chat.repository.ChatMessageRepository;
import com.bbangjun.realtimetrip.domain.chat.repository.ChatRoomRepository;
import com.bbangjun.realtimetrip.domain.user.entity.User;
import com.bbangjun.realtimetrip.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class ChatService {

    private final UserRepository userRepository;
    private final ChatRoomRepository chatRoomRepository;
    private final ChatMessageRepository chatMessageRepository;
    private final RedisTemplate<String, String> redisTemplate;
    private final ChannelTopic channelTopic;

    @Transactional
    public void sendMessage(ChatMessageDto chatMessageDto) {

        Optional<User> optionalUser = userRepository.findById(chatMessageDto.getUserId());
        User user = optionalUser.get();

        ChatRoom chatRoom = chatRoomRepository.findByRoomId(chatMessageDto.getRoomId());

        //채팅 생성 및 저장
        ChatMessage newChatMessage = ChatMessage.builder()
                .messageType(ChatMessage.MessageType.TALK)
                .chatRoom(chatRoom)
                .user(user)
                .message(chatMessageDto.getMessage())
                .sendTime(LocalDateTime.now())
                .build();

        chatMessageRepository.save(newChatMessage);
        String topic = channelTopic.getTopic(); // ch01

        // ChatMessageRequest에 유저정보, 현재시간 저장
        chatMessageDto.setNickName(user.getNickname());
        chatMessageDto.setUserId(user.getUserId());

        //if (chatMessageDto.getMessageType() == ChatMessageDto.MessageType.TALK) {
            // 그륩 채팅일 경우
            redisTemplate.convertAndSend(topic, chatMessageDto);
        //}

        // convertAndSend는 수신한 메시지를 지정된 topic으로 broadcasting하는 기능을 수행함.(핸들링한 메시지를 지정된 topic으로 메시지 전달)
        //simpMessageSendingOperations.convertAndSend("/sub/chat/room/" + chatMessage.getRoomId(), chatMessage);
    }
}
