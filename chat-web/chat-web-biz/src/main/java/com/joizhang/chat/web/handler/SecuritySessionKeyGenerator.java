package com.joizhang.chat.web.handler;

import org.springframework.web.socket.WebSocketSession;

public class SecuritySessionKeyGenerator implements SessionKeyGenerator {

    @Override
    public Object sessionKey(WebSocketSession webSocketSession) {
        Object obj = webSocketSession.getAttributes().get("USER_KEY_ATTR_NAME");
        return obj instanceof String ? String.valueOf(obj) : null;
    }
}
