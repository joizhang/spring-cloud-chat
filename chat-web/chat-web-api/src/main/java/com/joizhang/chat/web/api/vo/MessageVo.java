package com.joizhang.chat.web.api.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class MessageVo implements Serializable {

    /**
     * 发送消息的用户
     */
    private Long senderId;
    /**
     * 目标用户（告知 STOMP 代理转发到哪个用户）
     */
    private Long receiverId;

    /**
     * 消息内容
     */
    private String content;

    /**
     * 消息类型
     */
    private Integer contentType;

}
