package com.joizhang.chat.web.service.impl;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.joizhang.chat.web.api.constant.RabbitConstants;
import com.joizhang.chat.web.api.entity.ChatMessage;
import com.joizhang.chat.web.api.vo.MessageVo;
import com.joizhang.chat.web.mapper.ChatMessageMapper;
import com.joizhang.chat.web.service.ChatMessageService;
import com.joizhang.chat.web.util.WebSocketSessionHolder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
@Service
public class ChatMessageServiceImpl extends ServiceImpl<ChatMessageMapper, ChatMessage> implements ChatMessageService {

    private final RabbitTemplate rabbitTemplate;

    @Override
    public void sendToMQ(MessageVo messageVo) {
        rabbitTemplate.convertAndSend(RabbitConstants.DIRECT_MODE_QUEUE_ONE, messageVo);
    }

    @Override
    public void publishToCustomer(MessageVo messageVo) {
        ChatMessage entity = new ChatMessage();
        BeanUtils.copyProperties(messageVo, entity);
        baseMapper.insert(entity);
        String sessionKey = String.valueOf(messageVo.getReceiverId());
        WebSocketSession session = WebSocketSessionHolder.getSession(sessionKey);
        if (ObjectUtil.isNull(session)) {
            return;
        }
        String jsonStr = JSONUtil.toJsonStr(entity);
        try {
            session.sendMessage(new TextMessage(jsonStr));
        } catch (IOException e) {
            log.error("Socket connection error from {} to {}!",
                    messageVo.getSenderId(), messageVo.getReceiverId());
        }
    }
}
