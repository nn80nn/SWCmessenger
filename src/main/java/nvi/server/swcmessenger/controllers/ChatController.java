package nvi.server.swcmessenger.controllers;
import lombok.RequiredArgsConstructor;
import nvi.server.swcmessenger.databases.ChatMessageRepository;
import nvi.server.swcmessenger.models.ChatMessage;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;

import java.time.LocalDateTime;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class ChatController {

    private final ChatMessageRepository repository;

    @MessageMapping("/sendMessage")
    @SendTo("/topic/messages")
    public ChatMessage sendMessage(ChatMessage message, SimpMessageHeaderAccessor headerAccessor) {
        message.setTimestamp(LocalDateTime.now());
        repository.save(message);
        return message;
    }

    @MessageMapping("/history")
    @SendTo("/topic/history")
    public List<ChatMessage> getHistory() {
        return repository.findTop20ByOrderByTimestampDesc();
    }
}
