package com.joizhang.chat.admin.controller;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.joizhang.chat.admin.api.dto.UserDTO;
import com.joizhang.chat.admin.api.dto.UserInfo;
import com.joizhang.chat.admin.api.entity.SysUser;
import com.joizhang.chat.admin.api.vo.UserExcelVO;
import com.joizhang.chat.admin.api.vo.UserInfoVO;
import com.joizhang.chat.admin.api.vo.UserVO;
import com.joizhang.chat.admin.service.SysUserService;
import com.joizhang.chat.common.core.exception.ErrorCodes;
import com.joizhang.chat.common.core.util.MsgUtils;
import com.joizhang.chat.common.core.util.R;
import com.joizhang.chat.common.log.annotation.RecordSysLog;
import com.joizhang.chat.common.security.annotation.Inner;
import com.joizhang.chat.common.security.util.SecurityUtils;
import com.joizhang.chat.common.xss.core.XssCleanIgnore;
import com.pig4cloud.plugin.excel.annotation.RequestExcel;
import com.pig4cloud.plugin.excel.annotation.ResponseExcel;
import com.pig4cloud.plugin.excel.vo.ErrorMessage;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Set;

@RequiredArgsConstructor
@RestController
@RequestMapping("/user")
@Tag(name = "用户管理模块")
@SecurityRequirement(name = HttpHeaders.AUTHORIZATION)
public class SysUserController {

    private final SysUserService userService;

    /**
     * 获取当前用户全部信息
     *
     * @return 用户信息
     */
    @GetMapping(value = {"/info"})
    public R<UserInfoVO> info() {
        String username = SecurityUtils.getUser().getUsername();
        SysUser user = userService.getOne(Wrappers.<SysUser>query().lambda().eq(SysUser::getUsername, username));
        if (user == null) {
            return R.failed(MsgUtils.getMessage(ErrorCodes.SYS_USER_QUERY_ERROR));
        }
        UserInfo userInfo = userService.getUserInfo(user);
        UserInfoVO vo = new UserInfoVO();
        vo.setSysUser(userInfo.getSysUser());
        vo.setRoles(userInfo.getRoles());
        vo.setPermissions(userInfo.getPermissions());
        return R.ok(vo);
    }

    /**
     * 获取指定用户全部信息
     *
     * @return 用户信息
     */
    @Inner
    @GetMapping("/info/{username}")
    public R<UserInfo> info(@PathVariable String username) {
        SysUser user = userService.getOne(Wrappers.<SysUser>query().lambda().eq(SysUser::getUsername, username));
        if (user == null) {
            return R.failed(MsgUtils.getMessage(ErrorCodes.SYS_USER_USERINFO_EMPTY, username));
        }
        return R.ok(userService.getUserInfo(user));
    }

    /**
     * 根据部门id，查询对应的用户 id 集合
     *
     * @param deptIds 部门id 集合
     * @return 用户 id 集合
     */
    @Inner
    @GetMapping("/ids")
    public R<List<Long>> listUserIdByDeptIds(@RequestParam("deptIds") Set<Long> deptIds) {
        return R.ok(userService.listUserIdByDeptIds(deptIds));
    }

    /**
     * 通过ID查询用户信息
     *
     * @param id ID
     * @return 用户信息
     */
    @GetMapping("/{id:\\d+}")
    public R<UserVO> user(@PathVariable Long id) {
        return R.ok(userService.getUserVoById(id));
    }

    /**
     * 判断用户是否存在
     *
     * @param userDTO 查询条件
     * @return if exists return true
     */
    @Inner(false)
    @GetMapping("/check/exist")
    public R<Boolean> isExist(UserDTO userDTO) {
        List<SysUser> sysUserList = userService.list(new QueryWrapper<>(userDTO));
        if (CollUtil.isNotEmpty(sysUserList)) {
            return R.ok(Boolean.TRUE, MsgUtils.getMessage(ErrorCodes.SYS_USER_EXISTING));
        }
        return R.ok(Boolean.FALSE);
    }

    /**
     * 删除用户信息
     *
     * @param id ID
     * @return R
     */
    @RecordSysLog("删除用户信息")
    @DeleteMapping("/{id:\\d+}")
    @PreAuthorize("@pms.hasPermission('sys_user_del')")
    public R<Boolean> userDel(@PathVariable Long id) {
        SysUser sysUser = userService.getById(id);
        return R.ok(userService.removeUserById(sysUser));
    }

    /**
     * 添加用户
     *
     * @param userDto 用户信息
     * @return success/false
     */
    @RecordSysLog("添加用户")
    @PostMapping
    @XssCleanIgnore({"password"})
    @PreAuthorize("@pms.hasPermission('sys_user_add')")
    public R<Boolean> user(@RequestBody UserDTO userDto) {
        return R.ok(userService.saveUser(userDto));
    }

    /**
     * 管理员更新用户信息
     *
     * @param userDto 用户信息
     * @return R
     */
    @RecordSysLog("更新用户信息")
    @PutMapping
    @XssCleanIgnore({"password"})
    @PreAuthorize("@pms.hasPermission('sys_user_edit')")
    public R<Boolean> updateUser(@Valid @RequestBody UserDTO userDto) {
        return userService.updateUser(userDto);
    }

    /**
     * 分页查询用户
     *
     * @param page    参数集
     * @param userDTO 查询参数列表
     * @return 用户集合
     */
    @GetMapping("/page")
    public R<IPage<UserVO>> getUserPage(Page page, UserDTO userDTO) {
        return R.ok(userService.getUserWithRolePage(page, userDTO));
    }

    /**
     * 个人修改个人信息
     *
     * @param userDto userDto
     * @return success/false
     */
    @RecordSysLog("修改个人信息")
    @PutMapping("/edit")
    @XssCleanIgnore({"password", "newpassword1"})
    public R<Boolean> updateUserInfo(@Valid @RequestBody UserDTO userDto) {
        userDto.setUsername(SecurityUtils.getUser().getUsername());
        return userService.updateUserInfo(userDto);
    }

    /**
     * @param username 用户名称
     * @return 上级部门用户列表
     */
    @GetMapping("/ancestor/{username}")
    public R<List<SysUser>> listAncestorUsers(@PathVariable String username) {
        return R.ok(userService.listAncestorUsersByUsername(username));
    }

    /**
     * 导出excel 表格
     *
     * @param userDTO 查询条件
     * @return excel
     */
    @ResponseExcel
    @GetMapping("/export")
    @PreAuthorize("@pms.hasPermission('sys_user_import_export')")
    public List<UserExcelVO> export(UserDTO userDTO) {
        return userService.listUser(userDTO);
    }

    /**
     * 导入用户
     *
     * @param excelVOList   用户列表
     * @param bindingResult 错误信息列表
     * @return R
     */
    @PostMapping("/import")
    @PreAuthorize("@pms.hasPermission('sys_user_import_export')")
    public R<List<ErrorMessage>> importUser(@RequestExcel List<UserExcelVO> excelVOList,
                                            BindingResult bindingResult) {
        return userService.importUser(excelVOList, bindingResult);
    }

}