package com.joizhang.chat.web.handler;

import com.joizhang.chat.web.handler.PlainTextMessageHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.socket.WebSocketSession;

@Slf4j
public class CustomPlainTextMessageHandler implements PlainTextMessageHandler {

    @Override
    public void handle(WebSocketSession session, String message) {
        log.info("sessionId {} ,msg {}", session.getId(), message);
    }
}
