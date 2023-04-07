package com.joizhang.chat.gateway.filter;

import cn.hutool.core.util.CharsetUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.Mode;
import cn.hutool.crypto.Padding;
import cn.hutool.crypto.symmetric.AES;
import cn.hutool.http.HttpUtil;
import com.joizhang.chat.common.core.constant.SecurityConstants;
import com.joizhang.chat.gateway.config.GatewayConfigProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.cloud.gateway.filter.factory.rewrite.CachedBodyOutputMessage;
import org.springframework.cloud.gateway.support.BodyInserterContext;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ReactiveHttpOutputMessage;
import org.springframework.http.codec.HttpMessageReader;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpRequestDecorator;
import org.springframework.lang.NonNull;
import org.springframework.web.reactive.function.BodyInserter;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.HandlerStrategies;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.Charset;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

/**
 * 密码解密工具类
 */
@Slf4j
@RequiredArgsConstructor
public class PasswordDecoderFilter extends AbstractGatewayFilterFactory<Object> {

    private static final List<HttpMessageReader<?>> messageReaders = HandlerStrategies.withDefaults().messageReaders();

    private static final String PASSWORD = "password";

    private static final String KEY_ALGORITHM = "AES";

    private final GatewayConfigProperties gatewayConfig;

    @Override
    public GatewayFilter apply(Object config) {
        return (exchange, chain) -> {
            ServerHttpRequest request = exchange.getRequest();

            // 1. 不是登录请求，直接向下执行
            boolean isAuthToken = StrUtil.containsAnyIgnoreCase(request.getURI().getPath(),
                    SecurityConstants.OAUTH_TOKEN_URL);
            if (!isAuthToken) {
                return chain.filter(exchange);
            }

            // 2. 刷新token类型，直接向下执行
            String grantType = request.getQueryParams().getFirst("grant_type");
            if (StrUtil.equals(SecurityConstants.REFRESH_TOKEN, grantType)) {
                return chain.filter(exchange);
            }

            // 3. 前端加密密文解密逻辑
            Class<String> inClass = String.class;
            Class<String> outClass = String.class;
            ServerRequest serverRequest = ServerRequest.create(exchange, messageReaders);

            // 4. 解密生成新的报文
            Mono<String> modifiedBody = serverRequest.bodyToMono(inClass).flatMap(decryptAES());

            BodyInserter<Mono<String>, ReactiveHttpOutputMessage> bodyInserter =
                    BodyInserters.fromPublisher(modifiedBody, outClass);
            HttpHeaders headers = new HttpHeaders();
            headers.putAll(exchange.getRequest().getHeaders());
            headers.remove(HttpHeaders.CONTENT_LENGTH);

            headers.set(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE);
            CachedBodyOutputMessage outputMessage = new CachedBodyOutputMessage(exchange, headers);
            return bodyInserter
                    .insert(outputMessage, new BodyInserterContext())
                    .then(Mono.defer(() -> {
                                ServerHttpRequest decorator = decorate(exchange, headers, outputMessage);
                                return chain.filter(exchange.mutate().request(decorator).build());
                            })
                    );
        };
    }

    /**
     * 原文解密
     */
    private Function<Object, Mono<String>> decryptAES() {
        return s -> {
            // 构建前端对应解密AES 因子
            AES aes = new AES(Mode.CFB, Padding.NoPadding,
                    new SecretKeySpec(gatewayConfig.getEncodeKey().getBytes(), KEY_ALGORITHM),
                    new IvParameterSpec(gatewayConfig.getEncodeKey().getBytes()));

            // 获取请求密码并解密
            Map<String, String> inParamsMap = HttpUtil.decodeParamMap((String) s, CharsetUtil.CHARSET_UTF_8);
            if (inParamsMap.containsKey(PASSWORD)) {
                String password = aes.decryptStr(inParamsMap.get(PASSWORD));
                // 返回修改后报文字符
                inParamsMap.put(PASSWORD, password);
            } else {
                log.error("非法请求数据:{}", s);
            }
            return Mono.just(HttpUtil.toParams(inParamsMap, Charset.defaultCharset(), true));
        };
    }

    /**
     * 报文转换
     */
    private ServerHttpRequestDecorator decorate(ServerWebExchange exchange,
                                                HttpHeaders headers,
                                                CachedBodyOutputMessage outputMessage) {
        return new ServerHttpRequestDecorator(exchange.getRequest()) {
            @Override
            public @NonNull HttpHeaders getHeaders() {
                long contentLength = headers.getContentLength();
                HttpHeaders httpHeaders = new HttpHeaders();
                httpHeaders.putAll(super.getHeaders());
                if (contentLength > 0) {
                    httpHeaders.setContentLength(contentLength);
                } else {
                    httpHeaders.set(HttpHeaders.TRANSFER_ENCODING, "chunked");
                }
                return httpHeaders;
            }

            @Override
            public @NonNull Flux<DataBuffer> getBody() {
                return outputMessage.getBody();
            }
        };
    }

}
