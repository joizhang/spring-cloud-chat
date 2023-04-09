package com.joizhang.chat.gateway.handler;

import com.joizhang.chat.common.core.constant.CacheConstants;
import com.joizhang.chat.common.core.constant.SecurityConstants;
import com.pig4cloud.captcha.ArithmeticCaptcha;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.lang.NonNull;
import org.springframework.util.FastByteArrayOutputStream;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.HandlerFunction;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Slf4j
@RequiredArgsConstructor
public class ImageCodeHandler implements HandlerFunction<ServerResponse> {

    private static final Integer DEFAULT_IMAGE_WIDTH = 100;

    private static final Integer DEFAULT_IMAGE_HEIGHT = 40;

    private final RedisTemplate<String, Object> redisTemplate;

    @Override
    public @NonNull Mono<ServerResponse> handle(@NonNull ServerRequest request) {
        ArithmeticCaptcha captcha = new ArithmeticCaptcha(DEFAULT_IMAGE_WIDTH, DEFAULT_IMAGE_HEIGHT);
        String result = captcha.text();
        // 保存验证码信息
        Optional<String> randomStr = request.queryParam("randomStr");
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        randomStr.ifPresent(s ->
                redisTemplate.opsForValue().set(
                        CacheConstants.DEFAULT_CODE_KEY + s, result,
                        SecurityConstants.CODE_TIME,
                        TimeUnit.SECONDS
                )
        );
        // 转换流信息写出
        FastByteArrayOutputStream os = new FastByteArrayOutputStream();
        captcha.out(os);
        return ServerResponse
                .status(HttpStatus.OK)
                .contentType(MediaType.IMAGE_JPEG)
                .body(BodyInserters.fromResource(new ByteArrayResource(os.toByteArray())));
    }

}
