package com.joizhang.chat.admin.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.joizhang.chat.admin.api.entity.SysRoleMenu;
import com.joizhang.chat.admin.mapper.SysRoleMenuMapper;
import com.joizhang.chat.admin.service.SysRoleMenuService;
import com.joizhang.chat.common.core.constant.CacheConstants;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * <p>
 * 角色菜单表 服务实现类
 * </p>
 */
@Service
@RequiredArgsConstructor
public class SysRoleMenuServiceImpl extends ServiceImpl<SysRoleMenuMapper, SysRoleMenu> implements SysRoleMenuService {

    private final CacheManager cacheManager;

    /**
     * @param roleId  角色
     * @param menuIds 菜单ID拼成的字符串，每个id之间根据逗号分隔
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean saveRoleMenus(Long roleId, String menuIds) {
        this.remove(Wrappers.<SysRoleMenu>query().lambda().eq(SysRoleMenu::getRoleId, roleId));

        if (StrUtil.isBlank(menuIds)) {
            return Boolean.TRUE;
        }
        List<SysRoleMenu> roleMenuList = Arrays.stream(menuIds.split(StrUtil.COMMA)).map(menuId -> {
            SysRoleMenu roleMenu = new SysRoleMenu();
            roleMenu.setRoleId(roleId);
            roleMenu.setMenuId(Long.valueOf(menuId));
            return roleMenu;
        }).collect(Collectors.toList());

        // 清空userinfo
        Objects.requireNonNull(cacheManager.getCache(CacheConstants.USER_DETAILS)).clear();
        // 清空全部的菜单缓存 fix #I4BM58
        Objects.requireNonNull(cacheManager.getCache(CacheConstants.MENU_DETAILS)).clear();
        return this.saveBatch(roleMenuList);
    }

}
