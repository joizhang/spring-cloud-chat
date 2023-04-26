package com.joizhang.chat.web.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.joizhang.chat.web.api.entity.ChatMessage;
import com.joizhang.chat.web.api.vo.MessageVo;

/**
 * 聊天消息 Service
 */
public interface ChatMessageService extends IService<ChatMessage> {

    /**
     * 发送至消息队列
     *
     * @param messageVo 消息
     */
    void sendToMQ(MessageVo messageVo);

    /**
     * 发送给消息接收者
     *
     * @param messageVo 消息
     */
    void consume(MessageVo messageVo);
}
