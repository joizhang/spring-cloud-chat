package com.joizhang.chat.web.api.feign;

import com.joizhang.chat.common.core.constant.SecurityConstants;
import com.joizhang.chat.common.core.constant.ServiceNameConstants;
import com.joizhang.chat.common.core.util.R;
import com.joizhang.chat.web.api.dto.CustomerInfo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * 聊天用户信息客户端
 */
@FeignClient(contextId = "remoteChatCustomerService", value = ServiceNameConstants.WEB_SERVICE)
public interface RemoteChatCustomerService {

    /**
     * 通过用户名查询用户、角色信息
     *
     * @param username 用户名
     * @return R
     */
    @GetMapping(value = "/svc/customer/info/{username}", headers = SecurityConstants.HEADER_FROM_IN)
    R<CustomerInfo> infoByUsername(@PathVariable("username") String username);

}
