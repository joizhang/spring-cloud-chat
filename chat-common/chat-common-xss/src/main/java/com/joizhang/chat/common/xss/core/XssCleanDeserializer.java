package com.joizhang.chat.common.xss.core;

import com.joizhang.chat.common.core.util.SpringContextHolder;
import com.joizhang.chat.common.xss.config.XssProperties;
import com.joizhang.chat.common.xss.utils.XssUtil;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

/**
 * jackson xss 处理
 */
@Slf4j
public class XssCleanDeserializer extends XssCleanDeserializerBase {

    @Override
    public String clean(String name, String text) throws IOException {
        // 读取 xss 配置
        XssProperties properties = SpringContextHolder.getBean(XssProperties.class);
        // 读取 XssCleaner bean
        XssCleaner xssCleaner = SpringContextHolder.getBean(XssCleaner.class);
        if (xssCleaner != null) {
            String value = xssCleaner.clean(XssUtil.trim(text, properties.isTrimText()));
            log.debug("Json property value:{} cleaned up by mica-xss, current value is:{}.", text, value);
            return value;
        } else {
            return XssUtil.trim(text, properties.isTrimText());
        }
    }

}
