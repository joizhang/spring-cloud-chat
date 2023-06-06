package com.joizhang.chat.web.api.entity;

import io.swagger.v3.oas.annotations.media.Schema;

import javax.validation.constraints.NotNull;

/**
 * 聊天群成员
 */
public class ChatGroupMember {

    @NotNull(message = "聊天群的id不能为空")
    @Schema(description = "聊天群的id")
    private Long groupId;

    @NotNull(message = "用户id不能为空")
    @Schema(description = "用户id")
    private Long customerId;

}
