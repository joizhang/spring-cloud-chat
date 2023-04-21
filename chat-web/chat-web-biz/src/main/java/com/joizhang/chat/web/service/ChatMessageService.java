package com.joizhang.chat.web.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.joizhang.chat.web.api.entity.ChatMessage;

/**
 * 聊天消息 Service
 */
public interface ChatMessageService extends IService<ChatMessage> {

    void send(String topic, String jsonValue);

}
