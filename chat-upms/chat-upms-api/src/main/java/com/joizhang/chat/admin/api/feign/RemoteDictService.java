package com.joizhang.chat.admin.api.feign;

import com.joizhang.chat.admin.api.entity.SysDictItem;
import com.joizhang.chat.common.core.constant.ServiceNameConstants;
import com.joizhang.chat.common.core.util.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

/**
 * 查询参数相关
 */
@FeignClient(contextId = "remoteDictService", value = ServiceNameConstants.UMPS_SERVICE)
public interface RemoteDictService {

    /**
     * 通过字典类型查找字典
     *
     * @param type 字典类型
     * @return 同类型字典
     */
    @GetMapping("/dict/type/{type}")
    R<List<SysDictItem>> getDictByType(@PathVariable("type") String type);

}
