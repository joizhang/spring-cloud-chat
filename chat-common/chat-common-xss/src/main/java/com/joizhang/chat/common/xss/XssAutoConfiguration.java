package com.joizhang.chat.common.xss;

import com.joizhang.chat.common.xss.config.XssProperties;
import com.joizhang.chat.common.xss.core.*;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.core.Ordered;
import org.springframework.lang.NonNull;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

/**
 * jackson xss 配置
 */
@RequiredArgsConstructor
@AutoConfiguration
@EnableConfigurationProperties(XssProperties.class)
@ConditionalOnProperty(prefix = XssProperties.PREFIX, name = "enabled", havingValue = "true", matchIfMissing = true)
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
public class XssAutoConfiguration implements WebMvcConfigurer {

    private final XssProperties xssProperties;

    @Bean
    @ConditionalOnMissingBean
    public XssCleaner xssCleaner(XssProperties properties) {
        return new DefaultXssCleaner(properties);
    }

    @Bean
    public FormXssClean formXssClean(XssProperties properties, XssCleaner xssCleaner) {
        return new FormXssClean(properties, xssCleaner);
    }

    @Bean
    public Jackson2ObjectMapperBuilderCustomizer xssJacksonCustomizer(XssProperties properties,
                                                                      XssCleaner xssCleaner) {
        return builder -> builder.deserializerByType(String.class, new JacksonXssClean(properties, xssCleaner));
    }

    @Override
    public void addInterceptors(@NonNull InterceptorRegistry registry) {
        List<String> patterns = xssProperties.getPathPatterns();
        if (patterns.isEmpty()) {
            patterns.add("/**");
        }
        XssCleanInterceptor interceptor = new XssCleanInterceptor(xssProperties);
        registry.addInterceptor(interceptor)
                .addPathPatterns(patterns)
                .excludePathPatterns(xssProperties.getPathExcludePatterns())
                .order(Ordered.LOWEST_PRECEDENCE);
    }

}
