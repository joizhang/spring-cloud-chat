package com.joizhang.chat.admin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.joizhang.chat.admin.api.entity.SysDict;

/**
 * 字典表
 */
public interface SysDictService extends IService<SysDict> {

    /**
     * 根据ID 删除字典
     *
     * @param id
     * @return
     */
    void removeDict(Long id);

    /**
     * 更新字典
     *
     * @param sysDict 字典
     * @return
     */
    void updateDict(SysDict sysDict);

    /**
     * 清除缓存
     */
    void clearDictCache();

}
