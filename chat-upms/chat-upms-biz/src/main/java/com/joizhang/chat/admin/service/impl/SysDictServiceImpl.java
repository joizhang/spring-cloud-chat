package com.joizhang.chat.admin.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.joizhang.chat.admin.api.entity.SysDict;
import com.joizhang.chat.admin.api.entity.SysDictItem;
import com.joizhang.chat.admin.mapper.SysDictItemMapper;
import com.joizhang.chat.admin.mapper.SysDictMapper;
import com.joizhang.chat.admin.service.SysDictService;
import com.joizhang.chat.common.core.constant.CacheConstants;
import com.joizhang.chat.common.core.enums.DictTypeEnum;
import com.joizhang.chat.common.core.exception.ErrorCodes;
import com.joizhang.chat.common.core.util.MsgUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

/**
 * 字典表
 */
@Service
@RequiredArgsConstructor
public class SysDictServiceImpl extends ServiceImpl<SysDictMapper, SysDict> implements SysDictService {

    private final SysDictItemMapper dictItemMapper;

    /**
     * 根据ID 删除字典
     *
     * @param id 字典ID
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    @CacheEvict(value = CacheConstants.DICT_DETAILS, allEntries = true)
    public void removeDict(Long id) {
        SysDict dict = this.getById(id);
        // 系统内置
        Assert.state(!DictTypeEnum.SYSTEM.getType().equals(dict.getSystemFlag()),
                MsgUtils.getMessage(ErrorCodes.SYS_DICT_DELETE_SYSTEM));
        baseMapper.deleteById(id);
        dictItemMapper.delete(Wrappers.<SysDictItem>lambdaQuery().eq(SysDictItem::getDictId, id));
    }

    /**
     * 更新字典
     *
     * @param dict 字典
     * @return
     */
    @Override
    @CacheEvict(value = CacheConstants.DICT_DETAILS, key = "#dict.dictKey")
    public void updateDict(SysDict dict) {
        SysDict sysDict = this.getById(dict.getId());
        // 系统内置
        Assert.state(!DictTypeEnum.SYSTEM.getType().equals(sysDict.getSystemFlag()),
                MsgUtils.getMessage(ErrorCodes.SYS_DICT_UPDATE_SYSTEM));
        this.updateById(dict);
    }

    @Override
    @CacheEvict(value = CacheConstants.DICT_DETAILS, allEntries = true)
    public void clearDictCache() {

    }

}
