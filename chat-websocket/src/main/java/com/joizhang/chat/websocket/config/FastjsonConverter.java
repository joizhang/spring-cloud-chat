package com.joizhang.chat.websocket.config;

import com.alibaba.fastjson.support.config.FastJsonConfig;
import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.StringHttpMessageConverter;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

@Configuration
public class FastjsonConverter {

    @Bean
    public HttpMessageConverters customConverters() {
        // 定义一个转换消息的对象
        FastJsonHttpMessageConverter fastJsonConverter = new FastJsonHttpMessageConverter();
        // 添加fastjson的配置信息 比如 ：是否要格式化返回的json数据
        FastJsonConfig fastJsonConfig = new FastJsonConfig();
        List<MediaType> fastMediaTypes = new ArrayList<MediaType>();
        fastJsonConfig.setCharset(StandardCharsets.UTF_8);
        fastMediaTypes.add(MediaType.APPLICATION_JSON);
        fastJsonConverter.setSupportedMediaTypes(fastMediaTypes);
        // 在转换器中添加配置信息
        fastJsonConverter.setFastJsonConfig(fastJsonConfig);

        StringHttpMessageConverter stringConverter = new StringHttpMessageConverter();
        stringConverter.setDefaultCharset(StandardCharsets.UTF_8);
        stringConverter.setSupportedMediaTypes(fastMediaTypes);

        return new HttpMessageConverters(stringConverter, fastJsonConverter);
    }

}
