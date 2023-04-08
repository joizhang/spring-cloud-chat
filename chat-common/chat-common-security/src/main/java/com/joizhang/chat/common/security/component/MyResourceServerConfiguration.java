package com.joizhang.chat.common.security.component;

import cn.hutool.core.util.ArrayUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.oauth2.server.resource.introspection.OpaqueTokenIntrospector;
import org.springframework.security.web.SecurityFilterChain;

/**
 * 资源服务器认证授权配置
 */
@Slf4j
@RequiredArgsConstructor
@EnableWebSecurity
public class MyResourceServerConfiguration {

    protected final ResourceAuthExceptionEntryPoint resourceAuthExceptionEntryPoint;

    private final PermitAllUrlProperties permitAllUrl;

    private final MyBearerTokenResolver bearerTokenResolver;

    private final OpaqueTokenIntrospector customOpaqueTokenIntrospector;

    @Bean
    @Order(Ordered.HIGHEST_PRECEDENCE)
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.authorizeRequests(authorizeRequests -> authorizeRequests
                        .antMatchers(ArrayUtil.toArray(permitAllUrl.getUrls(), String.class))
                        .permitAll()
                        .anyRequest()
                        .authenticated()
                )
                .oauth2ResourceServer(oauth2 -> oauth2
                        .authenticationEntryPoint(resourceAuthExceptionEntryPoint)
                        .bearerTokenResolver(bearerTokenResolver)
                        .opaqueToken(token -> token.introspector(customOpaqueTokenIntrospector))
                )
                .headers()
                .frameOptions()
                .disable()
                .and()
                .csrf()
                .disable();
        return http.build();
    }
}
