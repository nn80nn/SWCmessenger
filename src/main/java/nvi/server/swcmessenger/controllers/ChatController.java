package nvi.server.swcmessenger.controllers;
import lombok.RequiredArgsConstructor;
import nvi.server.swcmessenger.databases.ChatMessageRepository;
import nvi.server.swcmessenger.models.ChatMessage;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class ChatController {   // Пока тестовая версия не основываться будет переписано с нуля

    private final ChatMessageRepository repository;

    @MessageMapping("/chat")
    @SendTo("/topic/messages")
    public ChatMessage handleMessage(ChatMessage message) {
        return message;
    }
 
    @MessageMapping("/history")
    @SendTo("/topic/history")
    public List<ChatMessage> getHistory() {
        return repository.findTop20ByOrderByTimestampDesc();
    }
}
