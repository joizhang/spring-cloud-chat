package com.joizhang.chat.common.security.component;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.joizhang.chat.common.core.constant.SecurityConstants;
import com.joizhang.chat.common.core.util.WebUtils;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.openfeign.security.OAuth2FeignRequestInterceptor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.server.resource.web.BearerTokenResolver;

import javax.servlet.http.HttpServletRequest;
import java.util.Collection;

/**
 * oauth2 feign token传递
 * <p>
 * 官方实现{@link OAuth2FeignRequestInterceptor}部分场景不适用
 */
@Slf4j
@RequiredArgsConstructor
public class MyOAuthRequestInterceptor implements RequestInterceptor {

    private final BearerTokenResolver tokenResolver;

    /**
     * Create a template with the header of provided name and extracted token </br>
     * <p>
     * 1. 如果使用 非web 请求，header 区别
     * <p>
     * 2. 根据authentication 还原请求token
     *
     * @param template the template
     */
    @Override
    public void apply(RequestTemplate template) {
        Collection<String> fromHeader = template.headers().get(SecurityConstants.FROM);
        // 带 from 请求直接跳过
        if (CollUtil.isNotEmpty(fromHeader) && fromHeader.contains(SecurityConstants.FROM_IN)) {
            return;
        }
        // 非 web 请求直接跳过
        if (WebUtils.getRequest().isEmpty()) {
            return;
        }
        HttpServletRequest request = WebUtils.getRequest().get();
        // 避免请求参数的 query token 无法传递
        String token = tokenResolver.resolve(request);
        if (StrUtil.isBlank(token)) {
            return;
        }
        String value = String.format("%s %s", OAuth2AccessToken.TokenType.BEARER.getValue(), token);
        template.header(HttpHeaders.AUTHORIZATION, value);
    }
}
