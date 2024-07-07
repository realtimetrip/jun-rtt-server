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
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
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
    private final RedisPublisherService redisPublisherService;
    private final RedisSubscriberService redisSubscriberService;
    private final RedisMessageListenerContainer redisMessageListener;

    @Transactional
    public void sendMessage(ChatMessageDto chatMessageDto) {
        if (ChatMessageDto.MessageType.TALK.equals(chatMessageDto.getType())) {

            Optional<User> optionalUser = userRepository.findById(chatMessageDto.getUserId());
            User user = optionalUser.get();

            ChatRoom chatRoom = chatRoomRepository.findByChatRoomId(chatMessageDto.getChatRoomId());

            //채팅 생성 및 저장
            ChatMessage newChatMessage = ChatMessage.builder()
                    .type(ChatMessage.MessageType.TALK)
                    .chatRoom(chatRoom)
                    .user(user)
                    .message(chatMessageDto.getMessage())
                    .eventTime(LocalDateTime.now())
                    .build();

            chatMessageRepository.save(newChatMessage);

            ChannelTopic topic = new ChannelTopic(chatMessageDto.getChatRoomId());

            // ChatMessageRequest에 유저정보, 현재시간 저장
            chatMessageDto.setChatId(newChatMessage.getChatId());
            chatMessageDto.setNickName(user.getNickname());
            chatMessageDto.setUserId(user.getUserId());
            chatMessageDto.setEventTime(LocalDateTime.now());

            redisPublisherService.publish(topic, chatMessageDto);
        }
    }

    @Transactional
    public ChatMessageDto enterUser(ChatMessageDto chatMessageDto) {

        if (!ChatMessageDto.MessageType.ENTER.equals(chatMessageDto.getType())) {
            return null;
        }

        ChannelTopic topic = new ChannelTopic(chatMessageDto.getChatRoomId());
        redisMessageListener.addMessageListener(redisSubscriberService, topic);

        User user = userRepository.findById(chatMessageDto.getUserId()).get();

        chatMessageDto.setNickName(user.getNickname());
        chatMessageDto.setEventTime(LocalDateTime.now());
        chatMessageDto.setMessage(chatMessageDto.getNickName() + "님이 입장하셨습니다.");
        log.info("f chatMessageDto = {}", chatMessageDto);

        ChatRoom chatRoom = chatRoomRepository.findByChatRoomId(chatMessageDto.getChatRoomId());

        // 새로운 chatId를 생성하기 위해 시퀀스를 증가
        Long newChatId = chatRoom.getMessageSequence() + 1;
        chatRoom.setMessageSequence(newChatId);

        chatRoomRepository.save(chatRoom);

        ChatMessage chatMessage = ChatMessage.builder()
                .type(ChatMessage.MessageType.ENTER)
                .user(user)
                .chatRoom(chatRoom)
                .message(chatMessageDto.getMessage())
                .eventTime(chatMessageDto.getEventTime())
                .chatId(newChatId)
                .build();

        chatMessageRepository.save(chatMessage);

        chatMessageDto.setChatId(chatMessage.getChatId());

        // Websocket에 발행된 메시지를 redis로 발행(publish)
        redisPublisherService.publish(topic, chatMessageDto);

        return chatMessageDto;
    }
}
