package com.bbangjun.realtimetrip.domain.chat.service;

import com.bbangjun.realtimetrip.domain.chat.dto.EnterUserResponseDto;
import com.bbangjun.realtimetrip.domain.chat.dto.EnterUserRequestDto;
import com.bbangjun.realtimetrip.domain.chat.dto.SendMessageRequestDto;
import com.bbangjun.realtimetrip.domain.chat.dto.SendMessageResponseDto;
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
    public SendMessageResponseDto sendMessage(SendMessageRequestDto sendMessageRequestDto) {

        if (!SendMessageRequestDto.MessageType.TALK.equals(sendMessageRequestDto.getType())) {
            return null;
        }

        ChannelTopic topic = new ChannelTopic(sendMessageRequestDto.getChatRoomId());
        redisMessageListener.addMessageListener(redisSubscriberService, topic);

        User user = userRepository.findById(sendMessageRequestDto.getUserId()).get();

        ChatRoom chatRoom = chatRoomRepository.findByChatRoomId(sendMessageRequestDto.getChatRoomId());

        Long newChatId = chatRoom.getMessageSequence() + 1;
        chatRoom.setMessageSequence(newChatId);

        chatRoomRepository.save(chatRoom);

        SendMessageResponseDto sendMessageResponseDto = new SendMessageResponseDto();
        sendMessageResponseDto.setNickName(user.getNickname());
        sendMessageResponseDto.setUserId(sendMessageRequestDto.getUserId());
        sendMessageResponseDto.setType(SendMessageResponseDto.MessageType.TALK);
        sendMessageResponseDto.setMessage(sendMessageRequestDto.getMessage());
        sendMessageResponseDto.setChatRoomId(sendMessageRequestDto.getChatRoomId());
        sendMessageResponseDto.setEventTime(LocalDateTime.now());
        sendMessageResponseDto.setProfile(user.getProfile());

        //채팅 생성 및 저장
        ChatMessage chatMessage = ChatMessage.builder()
                .type(ChatMessage.MessageType.TALK)
                .user(user)
                .chatRoom(chatRoom)
                .message(sendMessageRequestDto.getMessage())
                .eventTime(LocalDateTime.now())
                .chatId(newChatId)
                .build();

        chatMessageRepository.save(chatMessage);

        sendMessageResponseDto.setChatId(chatMessage.getChatId());

        redisPublisherService.publish(topic, sendMessageResponseDto);

        return sendMessageResponseDto;
    }

    @Transactional
    public EnterUserResponseDto enterUser(EnterUserRequestDto enterUserRequestDto) {

        if (!EnterUserRequestDto.MessageType.ENTER.equals(enterUserRequestDto.getType())) {
            return null;
        }

        ChannelTopic topic = new ChannelTopic(enterUserRequestDto.getChatRoomId());
        redisMessageListener.addMessageListener(redisSubscriberService, topic);

        User user = userRepository.findById(enterUserRequestDto.getUserId()).get();

        log.info("enterUserRequestDto.getChatRoomId() = {}", enterUserRequestDto.getChatRoomId());

        ChatRoom chatRoom = chatRoomRepository.findByChatRoomId(enterUserRequestDto.getChatRoomId());


        // 새로운 chatId를 생성하기 위해 시퀀스를 증가
        Long newChatId = chatRoom.getMessageSequence() + 1;
        log.info("chatRoom = {}", chatRoom.getMessageSequence());
        chatRoom.setMessageSequence(newChatId);

        chatRoomRepository.save(chatRoom);
        log.info("here2");
        EnterUserResponseDto enterUserResponseDto = new EnterUserResponseDto();
        enterUserResponseDto.setNickName(user.getNickname());
        enterUserResponseDto.setUserId(enterUserRequestDto.getUserId());
        enterUserResponseDto.setType(EnterUserResponseDto.MessageType.ENTER);
        enterUserResponseDto.setMessage(user.getNickname() + "님이 입장하셨습니다.");
        enterUserResponseDto.setChatRoomId(enterUserRequestDto.getChatRoomId());
        enterUserResponseDto.setEventTime(LocalDateTime.now());

        ChatMessage chatMessage = ChatMessage.builder()
                .type(ChatMessage.MessageType.ENTER)
                .user(user)
                .chatRoom(chatRoom)
                .message(enterUserResponseDto.getMessage())
                .eventTime(enterUserResponseDto.getEventTime())
                .chatId(newChatId)
                .build();

        chatMessageRepository.save(chatMessage);

        log.info("here3");
        enterUserResponseDto.setChatId(chatMessage.getChatId());
        log.info("here4");
        // Websocket에 발행된 메시지를 redis로 발행(publish)
        redisPublisherService.publish(topic, enterUserResponseDto);
        log.info("here5");
        return enterUserResponseDto;
    }
}
