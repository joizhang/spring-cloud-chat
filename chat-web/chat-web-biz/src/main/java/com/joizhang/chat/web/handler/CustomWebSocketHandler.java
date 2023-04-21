package com.joizhang.chat.web.handler;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.joizhang.chat.web.handler.PlainTextMessageHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.NonNull;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

@Slf4j
public class CustomWebSocketHandler extends TextWebSocketHandler {

    private PlainTextMessageHandler planTextMessageHandler;

    public CustomWebSocketHandler() {
    }

    public CustomWebSocketHandler(PlainTextMessageHandler planTextMessageHandler) {
        this.planTextMessageHandler = planTextMessageHandler;
    }

    public void handleTextMessage(@NonNull WebSocketSession session, TextMessage message) throws JsonProcessingException {
        if (message.getPayloadLength() != 0) {
            this.planTextMessageHandler.handle(session, (String)message.getPayload());
        }
    }
}
