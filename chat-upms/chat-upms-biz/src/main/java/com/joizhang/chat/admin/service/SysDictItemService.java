package com.joizhang.chat.admin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.joizhang.chat.admin.api.entity.SysDictItem;

/**
 * 字典项
 */
public interface SysDictItemService extends IService<SysDictItem> {

    /**
     * 删除字典项
     *
     * @param id 字典项ID
     * @return
     */
    void removeDictItem(Long id);

    /**
     * 更新字典项
     *
     * @param item 字典项
     * @return
     */
    void updateDictItem(SysDictItem item);

}
