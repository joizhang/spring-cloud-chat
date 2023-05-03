package com.joizhang.chat.gateway.filter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.List;

/**
 * <p>
 * 全局拦截器，作用所有的微服务
 * <p>
 * 1. 对请求的API调用过滤，记录接口的请求时间，方便日志审计、告警、分析等运维操作
 * <p>
 * 2. 后期可以扩展对接其他日志系统
 *
 * @author zhangran
 */
@Slf4j
public class ApiLoggingFilter implements GlobalFilter, Ordered {

    private static final String START_TIME = "startTime";

    private static final String X_REAL_IP = "X-Real-IP"; // nginx需要配置

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest serverHttpRequest = exchange.getRequest();
        if (log.isDebugEnabled()) {
            String info = String.format(
                    "Method:{%s} Host:{%s} Path:{%s} Query:{%s}",
                    serverHttpRequest.getMethod().name(),
                    serverHttpRequest.getURI().getHost(),
                    serverHttpRequest.getURI().getPath(),
                    serverHttpRequest.getQueryParams()
            );
            log.debug(info);
        }
        exchange.getAttributes().put(START_TIME, System.currentTimeMillis());
        return chain.filter(exchange).then(Mono.fromRunnable(() -> {
            Long startTime = exchange.getAttribute(START_TIME);
            if (startTime != null) {
                Long executeTime = (System.currentTimeMillis() - startTime);
                List<String> ips = serverHttpRequest.getHeaders().get(X_REAL_IP);
                String ip = ips != null ? ips.get(0) : null;
                String api = serverHttpRequest.getURI().getRawPath();

                int code = HttpStatus.INTERNAL_SERVER_ERROR.value();
                if (exchange.getResponse().getStatusCode() != null) {
                    code = exchange.getResponse().getStatusCode().value();
                }
                // 当前仅记录日志，后续可以添加日志队列，来过滤请求慢的接口
                if (log.isDebugEnabled()) {
                    log.debug("来自IP地址：{}，请求接口：{}，响应状态码：{}，请求耗时：{}ms",
                            ip, api, code, executeTime);
                }
            }
        }));
    }

    @Override
    public int getOrder() {
        return Ordered.LOWEST_PRECEDENCE;
    }
}
