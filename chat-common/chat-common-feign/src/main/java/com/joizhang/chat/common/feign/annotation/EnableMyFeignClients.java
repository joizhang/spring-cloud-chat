package com.joizhang.chat.common.feign.annotation;

import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.cloud.openfeign.MyFeignClientsRegistrar;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@EnableFeignClients
@Import(MyFeignClientsRegistrar.class)
public @interface EnableMyFeignClients {
}
