package com.joizhang.chat.web.handler;

import org.springframework.web.socket.WebSocketSession;

public interface PlainTextMessageHandler {

    void handle(WebSocketSession session, String message);

}
