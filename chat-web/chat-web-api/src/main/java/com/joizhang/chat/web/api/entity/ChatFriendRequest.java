package com.joizhang.chat.web.api.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.joizhang.chat.common.mybatis.base.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotNull;

/**
 * 朋友请求表
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class ChatFriendRequest extends BaseEntity {

    @TableId(value = "id", type = IdType.ASSIGN_ID)
    @Schema(description = "朋友请求编号")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;

    @NotNull
    @Schema(description = "好友请求发送者")
    private Long senderId;

    @NotNull
    @Schema(description = "好友请求接收者")
    private Long receiverId;

    @NotNull
    @Schema(description = "好友请求状态：1-PENDING, 2-ACCEPTED, 3-DECLINED")
    private Integer status;
}
