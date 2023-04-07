package com.joizhang.chat.gateway.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.joizhang.chat.gateway.filter.ChatRequestGlobalFilter;
import com.joizhang.chat.gateway.filter.PasswordDecoderFilter;
import com.joizhang.chat.gateway.handler.GlobalExceptionHandler;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 网关配置
 */
@Configuration(proxyBeanMethods = false)
@EnableConfigurationProperties(GatewayConfigProperties.class)
public class GatewayConfiguration {

    @Bean
    public ChatRequestGlobalFilter chatRequestGlobalFilter() {
        return new ChatRequestGlobalFilter();
    }

    @Bean
    public PasswordDecoderFilter passwordDecoderFilter(GatewayConfigProperties configProperties) {
        return new PasswordDecoderFilter(configProperties);
    }

    @Bean
    public GlobalExceptionHandler globalExceptionHandler(ObjectMapper objectMapper) {
        return new GlobalExceptionHandler(objectMapper);
    }

}
