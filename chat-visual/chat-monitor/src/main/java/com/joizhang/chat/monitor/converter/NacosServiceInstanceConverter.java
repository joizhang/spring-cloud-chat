package com.joizhang.chat.monitor.converter;

import de.codecentric.boot.admin.server.cloud.discovery.DefaultServiceInstanceConverter;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.context.annotation.Configuration;
import org.springframework.lang.NonNull;

import java.util.Collections;
import java.util.Map;
import java.util.stream.Collectors;


/**
 * 针对 nacos 2.x 服务注册处理
 */
@Configuration(proxyBeanMethods = false)
public class NacosServiceInstanceConverter extends DefaultServiceInstanceConverter {

    @Override
    protected @NonNull Map<String, String> getMetadata(ServiceInstance instance) {
        if (instance.getMetadata() != null) {
            return instance.getMetadata().entrySet().stream()
                    .filter((e) -> e.getKey() != null && e.getValue() != null)
                    .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
        }
        return Collections.emptyMap();
    }

}
