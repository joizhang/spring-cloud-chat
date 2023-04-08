package com.joizhang.chat.admin.api.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.joizhang.chat.common.mybatis.base.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotBlank;

/**
 * <p>
 * 日志表
 * </p>
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class SysLog extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 编号
     */
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    @Schema(description = "日志编号")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;

    /**
     * 日志类型
     */
    @NotBlank(message = "日志类型不能为空")
    @Schema(description = "日志类型")
    private String type;

    /**
     * 日志标题
     */
    @NotBlank(message = "日志标题不能为空")
    @Schema(description = "日志标题")
    private String title;

    /**
     * 操作IP地址
     */
    @Schema(description = "操作ip地址")
    private String remoteAddr;

    /**
     * 用户浏览器
     */
    @Schema(description = "用户代理")
    private String userAgent;

    /**
     * 请求URI
     */
    @Schema(description = "请求uri")
    private String requestUri;

    /**
     * 操作方式
     */
    @Schema(description = "操作方式")
    private String method;

    /**
     * 操作提交的数据
     */
    @Schema(description = "数据")
    private String params;

    /**
     * 执行时间
     */
    @Schema(description = "方法执行时间")
    private Long time;

    /**
     * 异常信息
     */
    @Schema(description = "异常信息")
    private String exception;

    /**
     * 服务ID
     */
    @Schema(description = "应用标识")
    private String serviceId;

    /**
     * 删除标记
     */
    @TableLogic
    private String delFlag;

}
