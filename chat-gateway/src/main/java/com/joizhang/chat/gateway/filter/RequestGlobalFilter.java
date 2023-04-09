package com.joizhang.chat.gateway.filter;

import com.joizhang.chat.common.core.constant.CommonConstants;
import com.joizhang.chat.common.core.constant.SecurityConstants;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.cloud.gateway.support.ServerWebExchangeUtils;
import org.springframework.core.Ordered;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.Collections;
import java.util.stream.Collectors;

/**
 * <p>
 * 全局拦截器，作用所有的微服务
 * <p>
 * 1. 对请求头中参数进行处理 from 参数进行清洗
 * <p>
 * 2. 重写StripPrefix = 1,支持全局
 * <p>
 * 支持swagger添加X-Forwarded-Prefix header （F SR2 已经支持，不需要自己维护）
 */
public class RequestGlobalFilter implements GlobalFilter, Ordered {

    /**
     * Process the Web request and (optionally) delegate to the next {@code WebFilter}
     * through the given {@link GatewayFilterChain}.
     *
     * @param exchange the current server exchange
     * @param chain    provides a way to delegate to the next filter
     * @return {@code Mono<Void>} to indicate when request processing is complete
     */
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        // 1. 清洗请求头中from 参数
        ServerHttpRequest request = exchange.getRequest()
                .mutate()
                .headers(httpHeaders -> {
                    httpHeaders.remove(SecurityConstants.FROM);
                    // 设置请求时间
                    httpHeaders.put(CommonConstants.REQUEST_START_TIME,
                            Collections.singletonList(String.valueOf(System.currentTimeMillis())));
                })
                .build();

        // 2. 重写StripPrefix
        ServerWebExchangeUtils.addOriginalRequestUrl(exchange, request.getURI());
        String rawPath = request.getURI().getRawPath();
        String newPath = "/" + Arrays.stream(StringUtils.tokenizeToStringArray(rawPath, "/"))
                .skip(1L)
                .collect(Collectors.joining("/"));

        ServerHttpRequest newRequest = request.mutate().path(newPath).build();
        exchange.getAttributes().put(ServerWebExchangeUtils.GATEWAY_REQUEST_URL_ATTR, newRequest.getURI());

        return chain.filter(exchange.mutate().request(newRequest.mutate().build()).build());
    }

    @Override
    public int getOrder() {
        return 10;
    }
}
