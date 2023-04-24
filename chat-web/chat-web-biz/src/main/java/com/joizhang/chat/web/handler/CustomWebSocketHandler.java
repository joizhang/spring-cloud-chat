package com.joizhang.chat.web.handler;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.json.JSONUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.joizhang.chat.web.api.constant.MessageContentType;
import com.joizhang.chat.web.api.vo.MessageVo;
import com.joizhang.chat.web.service.ChatMessageService;
import com.joizhang.chat.web.util.WebSocketSessionHolder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.amqp.AmqpException;
import org.springframework.lang.NonNull;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.ConcurrentWebSocketSessionDecorator;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
public class CustomWebSocketHandler extends TextWebSocketHandler {

    private final SessionKeyGenerator sessionKeyGenerator;

    private final ChatMessageService messageService;

    @Override
    public void afterConnectionEstablished(@NonNull WebSocketSession session) throws Exception {
        super.afterConnectionEstablished(session);
        String sessionKey = this.sessionKeyGenerator.sessionKey(session);
        ConcurrentWebSocketSessionDecorator sessionDecorator =
                new ConcurrentWebSocketSessionDecorator(session, 5000, 10240);
        WebSocketSessionHolder.addSession(sessionKey, sessionDecorator);
    }

    protected void handleTextMessage(@NotNull WebSocketSession session, @NotNull TextMessage message) throws Exception {
        String sessionKey = this.sessionKeyGenerator.sessionKey(session);
        WebSocketSession sessionDecorator = WebSocketSessionHolder.getSession(sessionKey);
        String payload = message.getPayload();
        ObjectMapper objectMapper = new ObjectMapper();
        MessageVo errorMessage = null;
        try {
            MessageVo messageVo = objectMapper.readValue(payload, MessageVo.class);
            log.debug("sessionId {} ,msg {}", session.getId(), messageVo);
            messageService.sendToMQ(messageVo);
        } catch (JsonProcessingException e) {
            // 消息结构异常
            errorMessage = new MessageVo(0L, 0L,
                    "ERROR: ILLEGAL DATA FORMAT", MessageContentType.ERROR.getType());
        } catch (AmqpException e) {
            // 消息队列异常
            log.error("Message queue error: {}", e.getMessage());
            errorMessage = new MessageVo(0L, 0L,
                    "ERROR: MESSAGE QUEUE CONNECTION ERROR", MessageContentType.ERROR.getType());
        }
        if (ObjectUtil.isNotNull(errorMessage)) {
            String jsonStr = JSONUtil.toJsonStr(errorMessage);
            try {
                sessionDecorator.sendMessage(new TextMessage(jsonStr));
            } catch (IOException ex) {
                log.error("Socket connection error!");
            }
        }
    }

    @Override
    public void afterConnectionClosed(@NonNull WebSocketSession session,
                                      @NonNull CloseStatus closeStatus) throws Exception {
        super.afterConnectionClosed(session, closeStatus);
        String sessionKey = this.sessionKeyGenerator.sessionKey(session);
        WebSocketSessionHolder.removeSession(sessionKey);
    }
}
