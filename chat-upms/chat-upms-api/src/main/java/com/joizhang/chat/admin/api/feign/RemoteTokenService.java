package com.joizhang.chat.admin.api.feign;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.joizhang.chat.admin.api.vo.TokenVo;
import com.joizhang.chat.common.core.constant.SecurityConstants;
import com.joizhang.chat.common.core.constant.ServiceNameConstants;
import com.joizhang.chat.common.core.util.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Map;

/**
 * 令牌客户端
 */
@FeignClient(contextId = "remoteTokenService", value = ServiceNameConstants.AUTH_SERVICE)
public interface RemoteTokenService {

    /**
     * 分页查询token 信息
     *
     * @param params 分页参数
     * @return page
     */
    @PostMapping(value = "/token/page", headers = SecurityConstants.HEADER_FROM_IN)
    R<Page<TokenVo>> getTokenPage(@RequestBody Map<String, Object> params);

    /**
     * 删除token
     *
     * @param token token
     */
    @DeleteMapping(value = "/token/{token}", headers = SecurityConstants.HEADER_FROM_IN)
    R<Boolean> removeToken(@PathVariable("token") String token);

}
