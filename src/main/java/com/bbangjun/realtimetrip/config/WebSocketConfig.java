package com.bbangjun.realtimetrip.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Slf4j
@Configuration
@EnableWebSocketMessageBroker
@RequiredArgsConstructor
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config){
//        // websocket + stomp
//        config.enableSimpleBroker("/sub");
//        config.setApplicationDestinationPrefixes("/pub");

        // RabbitMQ 연동
        // 메시지 구독 url
        config.enableStompBrokerRelay("/exchange")
                .setClientLogin(rabbitUser)
                .setClientPasscode(rabbitPwd)
                .setSystemLogin(rabbitUser)
                .setSystemPasscode(rabbitPwd)
                .setRelayHost(rabbitHost)
                .setRelayPort(rabbitPort)
                .setVirtualHost(rabbitVHost);
        // 메시지 발행 url
        config.setPathMatcher(new AntPathMatcher("."));
        config.setApplicationDestinationPrefixes("/pub");
    }

    public void registerStompEndpoints(StompEndpointRegistry registry){
        registry.addEndpoint("/ws")
                .setAllowedOriginPatterns("*").withSockJS();
    }
}
