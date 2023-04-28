package com.joizhang.chat.web.service.impl;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.joizhang.chat.web.api.constant.RabbitConstants;
import com.joizhang.chat.web.api.entity.ChatMessage;
import com.joizhang.chat.web.mapper.ChatMessageMapper;
import com.joizhang.chat.web.service.ChatMessageService;
import com.joizhang.chat.web.util.WebSocketSessionHolder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class ChatMessageServiceImpl extends ServiceImpl<ChatMessageMapper, ChatMessage> implements ChatMessageService {

    private final RabbitTemplate rabbitTemplate;

    @Override
    public void sendToMQ(ChatMessage chatMessage) {
        rabbitTemplate.convertAndSend(RabbitConstants.DIRECT_MODE_QUEUE_ONE, chatMessage);
    }

    @Override
    public void consume(ChatMessage chatMessage) {
        baseMapper.insert(chatMessage);
        String sessionKey = String.valueOf(chatMessage.getReceiverId());
        WebSocketSession session = WebSocketSessionHolder.getSession(sessionKey);
        if (ObjectUtil.isNull(session)) {
            return;
        }
        String jsonStr = JSONUtil.toJsonStr(chatMessage);
        try {
            session.sendMessage(new TextMessage(jsonStr));
        } catch (IOException e) {
            log.error("Socket connection error from {} to {}!",
                    chatMessage.getSenderId(), chatMessage.getReceiverId());
        }
    }

    @Override
    public List<ChatMessage> getHistory(Long userId, Long serverStubId) {
        // 默认只获取三天之内的历史消息
        LocalDateTime threeDaysAgo = LocalDateTime.now()
                .withHour(0).withMinute(0).withSecond(0)
                .minus(3, ChronoUnit.DAYS);
        LambdaQueryWrapper<ChatMessage> queryWrapper = Wrappers.<ChatMessage>lambdaQuery()
                .eq(ChatMessage::getReceiverId, userId)
                .gt(ChatMessage::getId, serverStubId)
                .gt(ChatMessage::getCreateTime, threeDaysAgo);
        return this.list(queryWrapper);
    }
}
