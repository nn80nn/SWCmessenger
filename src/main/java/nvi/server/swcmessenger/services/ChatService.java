package nvi.server.swcmessenger.services;

import nvi.server.swcmessenger.databases.ChatMessageRepository;
import nvi.server.swcmessenger.models.ChatMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ChatService {

    @Autowired
    private ChatMessageRepository chatMessageRepository;

    public ChatMessage saveMessage(ChatMessage message) {
        return chatMessageRepository.save(message);
    }

    public List<ChatMessage> getRecentMessages() {
        return chatMessageRepository.findTop20ByOrderByTimestampDesc();
    }
}
