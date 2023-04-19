package com.joizhang.chat.admin.controller;

import com.joizhang.chat.admin.api.dto.UserDTO;
import com.joizhang.chat.admin.service.SysUserService;
import com.joizhang.chat.common.core.util.R;
import com.joizhang.chat.common.log.annotation.RecordSysLog;
import com.joizhang.chat.common.security.annotation.Inner;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 客户端注册功能 register.user = false
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("/register")
@ConditionalOnProperty(name = "register.user", matchIfMissing = true)
public class SysRegisterController {

    private final SysUserService userService;

    /**
     * 注册用户
     *
     * @param userDto 用户信息
     * @return success/false
     */
    @Inner(value = false)
    @RecordSysLog("注册用户")
    @PostMapping("/user")
    public R<Boolean> registerUser(@RequestBody UserDTO userDto) {
        return userService.registerUser(userDto);
    }

}
