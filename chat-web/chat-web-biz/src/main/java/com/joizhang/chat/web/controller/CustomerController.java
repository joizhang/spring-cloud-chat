package com.joizhang.chat.web.controller;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.joizhang.chat.common.core.exception.ErrorCodes;
import com.joizhang.chat.common.core.util.MsgUtils;
import com.joizhang.chat.common.core.util.R;
import com.joizhang.chat.common.security.annotation.Inner;
import com.joizhang.chat.common.security.util.SecurityUtils;
import com.joizhang.chat.web.api.dto.CustomerInfo;
import com.joizhang.chat.web.api.entity.ChatCustomer;
import com.joizhang.chat.web.api.vo.CustomerInfoVo;
import com.joizhang.chat.web.service.ChatCustomerService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/customer")
@Tag(name = "聊天用户模块")
@SecurityRequirement(name = HttpHeaders.AUTHORIZATION)
public class CustomerController {

    private ChatCustomerService customerService;

    @GetMapping("hello")
    public ResponseEntity<String> index() {
        return ResponseEntity.ok("hello");
    }

    /**
     * 获取当前用户全部信息
     *
     * @return 用户信息
     */
    @GetMapping(value = {"/info"})
    public R<CustomerInfoVo> info() {
        String username = SecurityUtils.getUser().getUsername();
        ChatCustomer customer = customerService
                .getOne(Wrappers.<ChatCustomer>query().lambda().eq(ChatCustomer::getUsername, username));
        if (customer == null) {
            return R.failed(MsgUtils.getMessage(ErrorCodes.SYS_USER_QUERY_ERROR));
        }
        CustomerInfo customerInfo = customerService.getCustomerInfo(customer);
        CustomerInfoVo customerInfoVo = new CustomerInfoVo();
        customerInfoVo.setCustomer(customerInfo.getChatCustomer());
//        UserInfoVO vo = new UserInfoVO();
//        vo.setSysUser(userInfo.getSysUser());
//        vo.setRoles(userInfo.getRoles());
//        vo.setPermissions(userInfo.getPermissions());
        return R.ok(customerInfoVo);
    }

    /**
     * 获取指定用户全部信息
     *
     * @return 用户信息
     */
    @Inner
    @GetMapping("/info/{username}")
    public R<CustomerInfo> info(@PathVariable String username) {
        ChatCustomer customer = customerService
                .getOne(Wrappers.<ChatCustomer>query().lambda().eq(ChatCustomer::getUsername, username));
        if (customer == null) {
            return R.failed(MsgUtils.getMessage(ErrorCodes.SYS_USER_USERINFO_EMPTY, username));
        }
        return R.ok(customerService.getCustomerInfo(customer));
    }
}
