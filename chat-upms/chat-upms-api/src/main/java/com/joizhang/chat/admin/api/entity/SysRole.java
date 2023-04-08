package com.joizhang.chat.admin.api.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.joizhang.chat.common.mybatis.base.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotBlank;

/**
 * <p>
 * 角色表
 * </p>
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class SysRole extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @TableId(value = "role_id", type = IdType.ASSIGN_ID)
    @Schema(description = "角色编号")
    private Long roleId;

    @NotBlank(message = "角色名称 不能为空")
    @Schema(description = "角色名称")
    private String roleName;

    @NotBlank(message = "角色标识 不能为空")
    @Schema(description = "角色标识")
    private String roleCode;

    @NotBlank(message = "角色描述 不能为空")
    @Schema(description = "角色描述")
    private String roleDesc;

    /**
     * 删除标识（0-正常,1-删除）
     */
    @TableLogic
    private String delFlag;

}
