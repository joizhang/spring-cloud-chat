package com.joizhang.chat.common.security.feign;

import feign.RequestInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.security.oauth2.server.resource.web.BearerTokenResolver;

/**
 * Feign Client 的 OAuth 配置
 */
public class MyFeignClientConfiguration {

    @Bean
    public RequestInterceptor oauthRequestInterceptor(BearerTokenResolver tokenResolver) {
        return new MyOAuthRequestInterceptor(tokenResolver);
    }
}
