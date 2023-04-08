package com.joizhang.chat.admin.api.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.joizhang.chat.common.mybatis.base.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 字典表
 */
@Data
@Schema(description = "字典类型")
@EqualsAndHashCode(callSuper = true)
public class SysDict extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 编号
     */
    @TableId(type = IdType.ASSIGN_ID)
    @Schema(description = "字典编号")
    private Long id;

    /**
     * 类型
     */
    @Schema(description = "字典key")
    private String dictKey;

    /**
     * 描述
     */
    @Schema(description = "字典描述")
    private String description;

    /**
     * 是否是系统内置
     */
    @Schema(description = "是否系统内置")
    private String systemFlag;

    /**
     * 备注信息
     */
    @Schema(description = "备注信息")
    private String remark;

    /**
     * 删除标记
     */
    @TableLogic
    @Schema(description = "删除标记,1:已删除,0:正常")
    private String delFlag;

}
