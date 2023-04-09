package com.joizhang.chat.auth;

import com.joizhang.chat.common.feign.annotation.EnableMyFeignClients;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * 认证授权中心
 */
@EnableMyFeignClients
@EnableDiscoveryClient
@SpringBootApplication
public class ChatAuthApplication {

    public static void main(String[] args) {
        SpringApplication.run(ChatAuthApplication.class, args);
    }

}
