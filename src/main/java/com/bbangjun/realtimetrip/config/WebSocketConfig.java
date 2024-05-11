package com.bbangjun.realtimetrip.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Slf4j
@Configuration
@EnableWebSocketMessageBroker // 웹소켓 활성화
@RequiredArgsConstructor
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {
    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        // 메시지 브로커의 Prefix를 등록
        // 클라이언트는 topic 구독 시 /sub 경로로 요청해야 함.
        config.enableSimpleBroker("/sub");
        //  클라이언트에서 메시지 발행 시 해당 메시지 매핑에 대한 접두사로 사용
        config.setApplicationDestinationPrefixes("/pub");
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        // 클라이언트에서 메시지 발행 시 해당 메시지 매핑에 대한 접두사로 사용됨.
        // 웹소켓 연결에 필요한 Endpoint를 지정: /ws-stomp
        // setAllowedOriginPatterns의 인자를 *로 설정하여 모든 출처에 대한 CORS 설정
        registry.addEndpoint("/ws-stomp").setAllowedOriginPatterns("*").withSockJS();
    }
}
