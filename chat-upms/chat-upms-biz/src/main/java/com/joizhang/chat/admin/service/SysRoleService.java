package com.joizhang.chat.admin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.joizhang.chat.admin.api.entity.SysRole;
import com.joizhang.chat.admin.api.vo.RoleExcelVO;
import com.joizhang.chat.common.core.util.R;
import org.springframework.validation.BindingResult;

import java.util.List;

/**
 * <p>
 * 角色 服务类
 * </p>
 */
public interface SysRoleService extends IService<SysRole> {

    /**
     * 通过角色ID，删除角色
     *
     * @param id the role id
     * @return true if deleted
     */
    Boolean removeRoleById(Long id);

    /**
     * 导入角色
     *
     * @param excelVOList   角色列表
     * @param bindingResult 错误信息列表
     * @return ok fail
     */
    R<?> importRole(List<RoleExcelVO> excelVOList, BindingResult bindingResult);

    /**
     * 查询全部的角色
     *
     * @return list
     */
    List<RoleExcelVO> listRole();

}
