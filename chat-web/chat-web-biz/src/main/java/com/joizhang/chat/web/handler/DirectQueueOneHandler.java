package com.joizhang.chat.web.handler;

import cn.hutool.json.JSONUtil;
import com.joizhang.chat.web.api.constant.RabbitConstants;
import com.joizhang.chat.web.api.vo.MessageVo;
import com.joizhang.chat.web.service.ChatMessageService;
import com.rabbitmq.client.Channel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
@Component
@RabbitListener(queues = RabbitConstants.DIRECT_MODE_QUEUE_ONE)
public class DirectQueueOneHandler {

    private final ChatMessageService messageService;

    @RabbitHandler
    public void directHandlerManualAck(MessageVo messageVo, Message message, Channel channel) {
        //  如果手动ACK，消息会被监听消费，但是消息在队列中依旧存在，如果未配置 acknowledge-mode 默认是会在消费完毕后自动ACK掉
        long deliveryTag = message.getMessageProperties().getDeliveryTag();
        try {
            log.debug("直接队列1，手动ACK，接收消息：{}", JSONUtil.toJsonStr(messageVo));
            // 通知 MQ 消息已被成功消费,可以ACK了
            channel.basicAck(deliveryTag, false);
            messageService.publishToCustomer(messageVo);
        } catch (Exception e) {
            try {
                // 处理失败,重新压入MQ
                channel.basicRecover();
            } catch (IOException e1) {
                log.error("MQ recover error from {} to {}",
                        messageVo.getSenderId(), messageVo.getReceiverId());
            }
        }
    }

}
