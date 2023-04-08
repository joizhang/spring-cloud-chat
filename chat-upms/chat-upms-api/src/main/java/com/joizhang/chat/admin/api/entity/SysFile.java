package com.joizhang.chat.admin.api.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.joizhang.chat.common.mybatis.base.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 文件管理
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class SysFile extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 编号
     */
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 文件名
     */
    private String fileName;

    /**
     * 原文件名
     */
    private String original;

    /**
     * 容器名称
     */
    private String bucketName;

    /**
     * 文件类型
     */
    private String type;

    /**
     * 文件大小
     */
    private Long fileSize;

    /**
     * 删除标识：1-删除，0-正常
     */
    @TableLogic
    private Integer delFlag;

}
