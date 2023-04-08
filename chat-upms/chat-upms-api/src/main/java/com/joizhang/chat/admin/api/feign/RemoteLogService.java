package com.joizhang.chat.admin.api.feign;

import com.joizhang.chat.admin.api.entity.SysLog;
import com.joizhang.chat.common.core.constant.SecurityConstants;
import com.joizhang.chat.common.core.constant.ServiceNameConstants;
import com.joizhang.chat.common.core.util.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 *
 */
@FeignClient(contextId = "remoteLogService", value = ServiceNameConstants.UMPS_SERVICE)
public interface RemoteLogService {

    /**
     * 保存日志
     *
     * @param sysLog 日志实体
     * @return success、false
     */
    @PostMapping(value = "/log", headers = SecurityConstants.HEADER_FROM_IN)
    R<Boolean> saveLog(@RequestBody SysLog sysLog);

}
