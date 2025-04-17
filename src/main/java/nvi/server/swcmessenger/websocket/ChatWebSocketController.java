package nvi.server.swcmessenger.websocket;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import nvi.server.swcmessenger.models.ChatMessage;
import nvi.server.swcmessenger.services.MessageQueueService;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.*;
import org.springframework.web.socket.handler.TextWebSocketHandler;

@Component
@RequiredArgsConstructor
public class ChatWebSocketController extends TextWebSocketHandler {

    private final MessageQueueService messageQueueService;

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        // Пример: сообщение в JSON {"sender":"...","getter":"...","content":"..."}
        ObjectMapper objectMapper = new ObjectMapper();
        ChatMessage chatMessage = objectMapper.readValue(message.getPayload(), ChatMessage.class);

        messageQueueService.handleIncomingMessage(chatMessage);
    }
}
