package com.joizhang.chat.monitor;

import de.codecentric.boot.admin.server.config.EnableAdminServer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * 监控中心
 *
 * @author lengleng
 * @since 2018年06月21日
 */
@EnableAdminServer
@EnableDiscoveryClient
@SpringBootApplication
public class ChatMonitorApplication {

    public static void main(String[] args) {
        SpringApplication.run(ChatMonitorApplication.class, args);
    }

}
