package nvi.server.swcmessenger.websocket;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import nvi.server.swcmessenger.models.ChatMessage;
import nvi.server.swcmessenger.services.ClientService;
import nvi.server.swcmessenger.services.MessageQueueService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.*;
import org.springframework.web.socket.handler.TextWebSocketHandler;

@Component
@RequiredArgsConstructor
public class AuthWebSocketHandler extends TextWebSocketHandler {

    private final ClientService clientService;
    private final MessageQueueService messageQueueService;
    private final SessionRegistry sessionRegistry;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        String token = getToken(session);
        String email = clientService.getEmailFromToken(token);
        if (email == null) {
            System.out.println("[WS] Invalid token: " + token);
            session.close(CloseStatus.NOT_ACCEPTABLE.withReason("Invalid token"));
            return;
        }
        sessionRegistry.put(email, session);
        session.getAttributes().put("email", email);
        System.out.println("[WS] Connected: " + email + ", sessionId=" + session.getId());
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        String email = (String) session.getAttributes().get("email");
        if (email != null) {
            sessionRegistry.remove(email);
            System.out.println("[WS] Disconnected: " + email + ", sessionId=" + session.getId());
        }
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        String email = (String) session.getAttributes().get("email");
        System.out.println("[WS] Message from " + email + ": " + message.getPayload());
        try {
            ChatMessage chatMessage = objectMapper.readValue(message.getPayload(), ChatMessage.class);
            messageQueueService.handleIncomingMessage(chatMessage);
        } catch (Exception e) {
            System.out.println("[WS] Ошибка при разборе сообщения: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private String getToken(WebSocketSession session) {
        String uri = session.getUri().toString();
        if (uri.contains("token=")) {
            String token = uri.substring(uri.indexOf("token=") + 6);
            int amp = token.indexOf('&');
            if (amp != -1) token = token.substring(0, amp);
            return token;
        }
        return null;
    }

    // Heartbeat: каждые 10 секунд отправлять ping всем подключённым сессиям
    @Scheduled(fixedRate = 10000)
    public void sendHeartbeat() {
        for (var entry : sessionRegistry.getAll().entrySet()) {
            WebSocketSession session = entry.getValue();
            if (session != null && session.isOpen()) {
                try {
                    session.sendMessage(new TextMessage("{\"type\":\"HEARTBEAT\",\"content\":\"ping from server\"}"));
                    System.out.println("[WS] Heartbeat sent to: " + entry.getKey());
                } catch (Exception e) {
                    System.out.println("[WS] Heartbeat error for: " + entry.getKey());
                    e.printStackTrace();
                }
            }
        }
    }
}
