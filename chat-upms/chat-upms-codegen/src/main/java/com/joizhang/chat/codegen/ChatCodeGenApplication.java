package com.joizhang.chat.codegen;

import com.joizhang.chat.common.datasource.annotation.EnableDynamicDataSource;
import com.joizhang.chat.common.feign.annotation.EnableMyFeignClients;
import com.joizhang.chat.common.security.annotation.EnableResourceServer;
import com.joizhang.chat.common.swagger.annotation.EnableSwaggerDoc;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * 代码生成模块
 */
@EnableSwaggerDoc
@EnableDynamicDataSource
@EnableMyFeignClients
@EnableResourceServer
@EnableDiscoveryClient
@SpringBootApplication
public class ChatCodeGenApplication {

    public static void main(String[] args) {
        SpringApplication.run(ChatCodeGenApplication.class, args);
    }

}
