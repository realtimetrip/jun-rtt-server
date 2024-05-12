package com.bbangjun.realtimetrip.domain.chat.service;

import com.bbangjun.realtimetrip.domain.chat.dto.ChatMessageDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RedisPublisherService {

    private final RedisTemplate<String, Object> redisTemplate;

    public void publish(ChannelTopic topic, ChatMessageDto chatMessageDto){
        // convertAndSend는 수신한 메시지를 지정된 topic으로 broadcasting하는 기능을 수행함.
        // 핸들링한 메시지를 지정된 topic으로 메시지 전달
        redisTemplate.convertAndSend(topic.getTopic(), chatMessageDto);
    }
}