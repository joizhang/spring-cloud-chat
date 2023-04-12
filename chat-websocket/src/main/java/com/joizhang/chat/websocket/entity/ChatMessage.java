package com.joizhang.chat.websocket.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.joizhang.chat.common.mybatis.base.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

//import javax.validation.constraints.NotBlank;

/**
 * 聊天消息
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class ChatMessage extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @TableId(value = "message_id", type = IdType.ASSIGN_ID)
    @Schema(description = "主键id")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long messageId;

    /**
     * 发送者ID
     */
//    @NotBlank(message = "发送者不能为空")
    @Schema(description = "发送者ID")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long messageFrom;

    /**
     * 接收者ID
     */
//    @NotBlank(message = "接收者不能为空")
    @Schema(description = "接收者ID")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long messageTo;

    /**
     * 内容
     */
    @Schema(description = "内容")
    private String content;

}
