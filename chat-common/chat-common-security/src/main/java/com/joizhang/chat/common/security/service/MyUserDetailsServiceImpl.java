package com.joizhang.chat.common.security.service;

import cn.hutool.core.util.ObjectUtil;
import com.joizhang.chat.admin.api.dto.UserInfo;
import com.joizhang.chat.admin.api.feign.RemoteUserService;
import com.joizhang.chat.common.core.constant.CacheConstants;
import com.joizhang.chat.common.core.util.R;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.Primary;
import org.springframework.security.core.userdetails.UserDetails;

/**
 * 用户详细信息
 */
@Slf4j
@RequiredArgsConstructor
@Primary
public class MyUserDetailsServiceImpl implements MyUserDetailsService {

    private final RemoteUserService remoteUserService;

    private final CacheManager cacheManager;

    /**
     * 用户名密码登录
     *
     * @param username 用户名
     * @return
     */
    @SneakyThrows
    @Override
    public UserDetails loadUserByUsername(String username) {
        Cache cache = cacheManager.getCache(CacheConstants.USER_DETAILS);
        if (ObjectUtil.isNotNull(cache) && ObjectUtil.isNotNull(cache.get(username))) {
            return (MyUser) cache.get(username).get();
        }
        R<UserInfo> result = remoteUserService.info(username);
        UserDetails userDetails = getUserDetails(result);
        if (cache != null) {
            cache.put(username, userDetails);
        }
        return userDetails;
    }

    @Override
    public int getOrder() {
        return Integer.MIN_VALUE;
    }
}
