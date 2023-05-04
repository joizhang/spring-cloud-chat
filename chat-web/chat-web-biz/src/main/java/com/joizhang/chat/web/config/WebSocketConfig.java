package com.joizhang.chat.web.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.joizhang.chat.web.handler.*;
import com.joizhang.chat.web.service.ChatMessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.lang.NonNull;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import java.util.List;

@RequiredArgsConstructor
@Configuration
@EnableWebSocket
@EnableConfigurationProperties({WebSocketProperties.class})
public class WebSocketConfig {

    private final WebSocketProperties webSocketProperties;

    @Bean
    @ConditionalOnMissingBean({SessionKeyGenerator.class})
    public SessionKeyGenerator sessionKeyGenerator() {
        return new SecuritySessionKeyGenerator();
    }

    @Bean
    @ConditionalOnMissingBean({HandshakeInterceptor.class})
    public HandshakeInterceptor handshakeInterceptor() {
        return new UserAttributeHandshakeInterceptor();
    }

    @Bean
    @ConditionalOnMissingBean({TextWebSocketHandler.class})
    public WebSocketHandler webSocketHandler(@Autowired(required = false) SessionKeyGenerator sessionKeyGenerator,
                                             ChatMessageService messageService, ObjectMapper objectMapper) {
        CustomWebSocketHandler customWebSocketHandler =
                new CustomWebSocketHandler(sessionKeyGenerator, messageService, objectMapper);
        return new MapSessionWebSocketHandlerDecorator(customWebSocketHandler, sessionKeyGenerator);
    }

    @Bean
    @ConditionalOnMissingBean
    public WebSocketConfigurer webSocketConfigurer(List<HandshakeInterceptor> handshakeInterceptor,
                                                   WebSocketHandler webSocketHandler) {
        return registry -> registry.addHandler(webSocketHandler, webSocketProperties.getPath())
                .addInterceptors(handshakeInterceptor.toArray(new HandshakeInterceptor[0]))
                .setAllowedOrigins(webSocketProperties.getAllowOrigins());
    }

}
