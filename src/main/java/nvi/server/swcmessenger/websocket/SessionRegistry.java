package nvi.server.swcmessenger.websocket;


import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketSession;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class SessionRegistry {
    private final Map<String, WebSocketSession> sessions = new ConcurrentHashMap<>();

    public void put(String email, WebSocketSession session) {
        sessions.put(email, session);
    }

    public void remove(String email) {
        sessions.remove(email);
    }

    public WebSocketSession get(String email) {
        return sessions.get(email);
    }

    public Map<String, WebSocketSession> getAll() {
        return sessions;
    }
}
