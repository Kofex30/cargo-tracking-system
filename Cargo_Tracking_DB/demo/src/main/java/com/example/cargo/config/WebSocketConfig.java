package com.example.cargo.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        // Mesaj broker ayarı: /topic prefix'i ile yayın yapacak
        config.enableSimpleBroker("/topic");
        // Client'tan gelen mesajlar /app prefix'i ile controller'a yönlendirilecek
        config.setApplicationDestinationPrefixes("/app");
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        // WebSocket bağlantı noktası (endpoint)
        registry.addEndpoint("/ws-cargo")
                .setAllowedOriginPatterns("*");
        registry.addEndpoint("/ws-cargo")
                .setAllowedOriginPatterns("*");
    }
}
