package com.joizhang.chat.gateway.config;

import com.joizhang.chat.gateway.filter.PasswordDecoderFilter;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;

import java.util.List;

@Data
@RefreshScope
@ConfigurationProperties("gateway")
public class GatewayConfigProperties {

    /**
     * 网关解密前端登录密码 秘钥 {@link PasswordDecoderFilter}
     */
    private String encodeKey;

}
