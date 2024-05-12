package com.bbangjun.realtimetrip.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class RedisConfig {

    @Bean
    public RedisConnectionFactory redisConnectionFactory() {
        return new LettuceConnectionFactory();
    }

    @Bean
    @Primary
    public RedisTemplate<String, String> stringValueRedisTemplate() {
        final RedisTemplate<String, String> redisTemplate = new RedisTemplate<>();

        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(new StringRedisSerializer());
        redisTemplate.setConnectionFactory(redisConnectionFactory());

        return redisTemplate;
    }

    /**
     * 메시지 수신을 위한 설정: RedisMessageListenerContainer
     *
     * Redis 의 channel 로부터 메시지를 수신받아 해당 MessageListenerAdapter 에게 디스패치
     */
    @Bean
    public RedisMessageListenerContainer redisContainer() {
        final RedisMessageListenerContainer container = new RedisMessageListenerContainer();

        container.setConnectionFactory(redisConnectionFactory());
        container.addMessageListener(messageStringListener(), channelTopic());

        return container;
    }

    /**
     * 메시지 수신을 위한 설정: MessageListenerAdapter
     * <p>
     * RedisMessageListenerContainer 로부터 메시지를 받아서 등록된 리스너(구독자)에게 전달
     */
    @Bean
    public MessageListener messageStringListener() {
        return new MessageListenerAdapter(new RedisMessageStringSubscriber());
    }

    /**
     * Redis 에서 주고 받을 채널 설정
     */
    @Bean
    public ChannelTopic channelTopic() {
        return new ChannelTopic("ch01");
    }
}
