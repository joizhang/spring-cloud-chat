package com.joizhang.chat.web.util;

import lombok.experimental.UtilityClass;
import org.springframework.web.socket.WebSocketSession;

import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@UtilityClass
public final class WebSocketSessionHolder {

    private static final Map<String, WebSocketSession> USER_SESSION_MAP = new ConcurrentHashMap<>();

    public static void addSession(Object sessionKey, WebSocketSession session) {
        USER_SESSION_MAP.put(sessionKey.toString(), session);
    }

    public static void removeSession(Object sessionKey) {
        USER_SESSION_MAP.remove(sessionKey.toString());
    }

    public static WebSocketSession getSession(Object sessionKey) {
        return USER_SESSION_MAP.get(sessionKey.toString());
    }

    public static Collection<WebSocketSession> getSessions() {
        return USER_SESSION_MAP.values();
    }

    public static Set<String> getSessionKeys() {
        return USER_SESSION_MAP.keySet();
    }
}
