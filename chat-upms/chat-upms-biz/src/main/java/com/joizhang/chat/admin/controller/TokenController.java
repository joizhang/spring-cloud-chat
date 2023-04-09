package com.joizhang.chat.admin.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.joizhang.chat.admin.api.feign.RemoteTokenService;
import com.joizhang.chat.admin.api.vo.TokenVo;
import com.joizhang.chat.common.core.util.R;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RequiredArgsConstructor
@RestController
@RequestMapping("/token")
@Tag(name = "令牌管理模块")
@SecurityRequirement(name = HttpHeaders.AUTHORIZATION)
public class TokenController {

    private final RemoteTokenService remoteTokenService;

    /**
     * 分页token 信息
     *
     * @param params 参数集
     * @return token集合
     */
    @GetMapping("/page")
    public R<Page<TokenVo>> token(@RequestParam Map<String, Object> params) {
        return remoteTokenService.getTokenPage(params);
    }

    /**
     * 删除
     *
     * @param token token
     * @return success/false
     */
    @DeleteMapping("/{token}")
    @PreAuthorize("@pms.hasPermission('sys_token_del')")
    public R<Boolean> delete(@PathVariable String token) {
        return remoteTokenService.removeToken(token);
    }

}
