package com.joizhang.chat.web.handler;

import org.springframework.web.socket.WebSocketSession;

public interface SessionKeyGenerator {

    Object sessionKey(WebSocketSession webSocketSession);

}
