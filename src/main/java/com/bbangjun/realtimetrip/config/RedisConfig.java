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
    import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
    import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
    import org.springframework.data.redis.serializer.StringRedisSerializer;

    @Configuration
    public class RedisConfig {

        @Bean
        public RedisMessageListenerContainer redisMessageListener(RedisConnectionFactory connectionFactory){
            RedisMessageListenerContainer container = new RedisMessageListenerContainer();
            container.setConnectionFactory(connectionFactory);
            return container;
        }

        /**
         * 어플리케이션에서 사용할 redisTemplate 설정
         */
        @Bean
        @Primary
        public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory connectionFactory){

            RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();

            redisTemplate.setConnectionFactory(connectionFactory);
            redisTemplate.setKeySerializer(new StringRedisSerializer());
            redisTemplate.setValueSerializer(new Jackson2JsonRedisSerializer<>(String.class));

            return redisTemplate;
        }
    }
