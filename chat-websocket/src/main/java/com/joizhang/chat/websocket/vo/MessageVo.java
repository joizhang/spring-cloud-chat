package com.joizhang.chat.websocket.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class MessageVo {

    /**
     * 发送消息的用户
     */
    private String from;
    /**
     * 目标用户（告知 STOMP 代理转发到哪个用户）
     */
    private String to;

    /**
     * 消息内容
     */
    private String content;

    /**
     * 广播转发的目标地址（告知 STOMP 代理转发到哪个地方）
     */
    private String destination;

}
