package nvi.server.swcmessenger.models;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Data
public class ChatMessage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String sender;
    private String content;
    private String getter;
    private LocalDateTime timestamp = LocalDateTime.now();
    private MessageType type;

    public enum MessageType {
        CHAT, JOIN, LEAVE
    }
}
