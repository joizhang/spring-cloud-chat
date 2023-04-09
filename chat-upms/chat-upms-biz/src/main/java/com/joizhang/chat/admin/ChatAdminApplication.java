package com.joizhang.chat.admin;

import com.joizhang.chat.common.feign.annotation.EnableMyFeignClients;
import com.joizhang.chat.common.security.annotation.EnableResourceServer;
import com.joizhang.chat.common.swagger.annotation.EnableSwaggerDoc;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * 用户统一管理系统
 */
@EnableSwaggerDoc
@EnableResourceServer
@EnableMyFeignClients
@EnableDiscoveryClient
@SpringBootApplication
public class ChatAdminApplication {

    public static void main(String[] args) {
        SpringApplication.run(ChatAdminApplication.class, args);
    }

}
