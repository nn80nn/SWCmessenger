package nvi.server.swcmessenger.databases;
import nvi.server.swcmessenger.models.ChatMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {
    List<ChatMessage> findTop20ByOrderByTimestampDesc();
}
