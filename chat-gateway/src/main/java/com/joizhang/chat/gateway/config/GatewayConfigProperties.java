package com.joizhang.chat.gateway.config;

import com.joizhang.chat.gateway.filter.ApiLoggingFilter;
import com.joizhang.chat.gateway.filter.PasswordDecoderFilter;
import com.joizhang.chat.gateway.filter.ValidateCodeGatewayFilter;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;

import java.util.List;

/**
 * @author lengleng
 */
@Data
@RefreshScope
@ConfigurationProperties("gateway")
public class GatewayConfigProperties {

    /**
     * 网关解密前端登录密码 秘钥 {@link PasswordDecoderFilter}
     */
    private String encodeKey;

    /**
     * 网关不需要校验验证码的客户端 {@link ValidateCodeGatewayFilter}
     */
    private List<String> ignoreClients;

    /**
     * 是否开启接口的日志{@link ApiLoggingFilter}
     */
    private boolean apiLogging;

    /**
     * 需要进行哈希负载均衡的服务
     */
    private String hashLbServiceName;

    /**
     * 哈希负载均衡器的类型
     */
    private String hashLBType;
}
