package com.joizhang.chat.gateway.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.joizhang.chat.gateway.filter.*;
import com.joizhang.chat.gateway.handler.GlobalExceptionHandler;
import com.joizhang.chat.gateway.handler.ImageCodeHandler;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.gateway.config.GatewayLoadBalancerProperties;
import org.springframework.cloud.loadbalancer.support.LoadBalancerClientFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;

/**
 * 网关配置
 *
 * @author L.cm
 * @author joizhang
 */
@Configuration(proxyBeanMethods = false)
@EnableConfigurationProperties(GatewayConfigProperties.class)
public class GatewayConfiguration {

    @Bean
    public RequestGlobalFilter requestGlobalFilter() {
        return new RequestGlobalFilter();
    }

    @Bean
    @ConditionalOnProperty(name = "gateway.api-logging")
    public ApiLoggingFilter apiLoggingFilter() {
        return new ApiLoggingFilter();
    }

    /**
     * 基于IP哈希和一致性哈希的客户端负载均衡过滤器
     */
    @Bean
    @ConditionalOnMissingBean(ReactiveHashLoadBalancerClientFilter.class)
    @ConditionalOnProperty(name = "gateway.hash-lb-service-name")
    public ReactiveHashLoadBalancerClientFilter gatewayHashLoadBalancerClientFilter(GatewayConfigProperties gatewayConfigProperties,
                                                                                    GatewayLoadBalancerProperties loadBalancerProperties,
                                                                                    LoadBalancerClientFactory clientFactory
    ) {
        return new ReactiveHashLoadBalancerClientFilter(gatewayConfigProperties, loadBalancerProperties, clientFactory);
    }

    @Bean
    @ConditionalOnProperty(name = "swagger.basic.enabled")
    public SwaggerBasicGatewayFilter swaggerBasicGatewayFilter(
            SpringDocConfiguration.SwaggerDocProperties swaggerProperties) {
        return new SwaggerBasicGatewayFilter(swaggerProperties);
    }

    /**
     * 验证码生成，{@link ImageCodeHandler} 会注入 {@link RouterFunctionConfiguration}
     */
    @Bean
    public ImageCodeHandler imageCodeHandler(RedisTemplate<String, Object> redisTemplate) {
        return new ImageCodeHandler(redisTemplate);
    }

    @Bean
    public ValidateCodeGatewayFilter validateCodeGatewayFilter(GatewayConfigProperties configProperties,
                                                               ObjectMapper objectMapper,
                                                               RedisTemplate<String, Object> redisTemplate) {
        return new ValidateCodeGatewayFilter(configProperties, objectMapper, redisTemplate);
    }

    /**
     * 密码解密
     */
    @Bean
    public PasswordDecoderFilter passwordDecoderFilter(GatewayConfigProperties configProperties) {
        return new PasswordDecoderFilter(configProperties);
    }

    @Bean
    public GlobalExceptionHandler globalExceptionHandler(ObjectMapper objectMapper) {
        return new GlobalExceptionHandler(objectMapper);
    }
}
