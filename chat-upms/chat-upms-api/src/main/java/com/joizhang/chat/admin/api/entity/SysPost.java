package com.joizhang.chat.admin.api.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.joizhang.chat.common.mybatis.base.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 岗位管理
 */
@Data
@TableName("sys_post")
@EqualsAndHashCode(callSuper = true)
@Schema(description = "岗位信息表")
public class SysPost extends BaseEntity {

    private static final long serialVersionUID = -8744622014102311894L;

    /**
     * 岗位ID
     */
    @TableId(type = IdType.ASSIGN_ID)
    @Schema(description = "岗位ID")
    private Long postId;

    /**
     * 岗位编码
     */
    @Schema(description = "岗位编码")
    private String postCode;

    /**
     * 岗位名称
     */
    @Schema(description = "岗位名称")
    private String postName;

    /**
     * 岗位排序
     */
    @Schema(description = "岗位排序")
    private Integer postSort;

    /**
     * 是否删除 -1：已删除 0：正常
     */
    @Schema(description = "是否删除  -1：已删除  0：正常")
    private String delFlag;

    /**
     * 备注信息
     */
    @Schema(description = "备注信息")
    private String remark;

}
