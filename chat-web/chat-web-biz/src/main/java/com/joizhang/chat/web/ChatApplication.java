package com.joizhang.chat.web;

import com.joizhang.chat.common.feign.annotation.EnableMyFeignClients;
import com.joizhang.chat.common.security.annotation.EnableResourceServer;
import com.joizhang.chat.common.swagger.annotation.EnableSwaggerDoc;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@EnableSwaggerDoc
@EnableResourceServer
@EnableMyFeignClients
@EnableDiscoveryClient
@SpringBootApplication
public class ChatApplication {

    public static void main(String[] args) {
        SpringApplication.run(ChatApplication.class, args);
    }

}
