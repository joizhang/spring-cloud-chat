package com.joizhang.chat.gateway.config;

import org.springframework.cloud.gateway.filter.ratelimit.KeyResolver;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.core.publisher.Mono;

import java.net.InetSocketAddress;
import java.util.Objects;

/**
 * 路由限流配置
 *
 * @author lengleng
 */
@Configuration(proxyBeanMethods = false)
public class RateLimiterConfiguration {

    @Bean
    public KeyResolver remoteAddrKeyResolver() {
        return exchange -> {
            InetSocketAddress address = Objects.requireNonNull(exchange.getRequest().getRemoteAddress());
            return Mono.just(address.getAddress().getHostAddress());
        };
    }

}
