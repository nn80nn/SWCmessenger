package nvi.server.swcmessenger.services;

import lombok.RequiredArgsConstructor;
import nvi.server.swcmessenger.models.ChatMessage;
import nvi.server.swcmessenger.websocket.SessionRegistry;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service
@RequiredArgsConstructor
public class MessageQueueService {

    private final SessionRegistry sessionRegistry;
    private final Map<String, Queue<ChatMessage>> messageQueues = new ConcurrentHashMap<>();
    private final ExecutorService executor = Executors.newCachedThreadPool();

    public void handleIncomingMessage(ChatMessage message) {
        String recipient = message.getGetter();
        System.out.println("[MQS] Получено сообщение для: " + recipient + " от: " + message.getSender());

        WebSocketSession session = sessionRegistry.get(recipient);
        System.out.println("[MQS] Сессия получателя: " + session);

        if (session != null && session.isOpen()) {
            try {
                String json = toJson(message);
                session.sendMessage(new TextMessage(json));
                System.out.println("[MQS] Сообщение отправлено онлайн-пользователю: " + recipient);
            } catch (Exception e) {
                System.out.println("[MQS] Ошибка при отправке онлайн-сообщения: " + e.getMessage());
                e.printStackTrace();
            }
        } else {
            System.out.println("[MQS] Получатель оффлайн, ставим сообщение в очередь: " + recipient);
            messageQueues.computeIfAbsent(recipient, k -> new ConcurrentLinkedQueue<>()).add(message);
            waitForUserOnlineAndSend(recipient);
        }
    }

    private void waitForUserOnlineAndSend(String recipient) {
        executor.submit(() -> {
            System.out.println("[MQS] Ожидание появления пользователя онлайн: " + recipient);
            while (true) {
                WebSocketSession session = sessionRegistry.get(recipient);
                if (session != null && session.isOpen()) {
                    Queue<ChatMessage> queue = messageQueues.get(recipient);
                    if (queue != null) {
                        ChatMessage msg;
                        while ((msg = queue.poll()) != null) {
                            try {
                                String json = toJson(msg);
                                session.sendMessage(new TextMessage(json));
                                System.out.println("[MQS] Доставлено сообщение из очереди пользователю: " + recipient);
                            } catch (Exception e) {
                                System.out.println("[MQS] Ошибка при доставке сообщения из очереди: " + e.getMessage());
                                e.printStackTrace();
                            }
                        }
                    }
                    break;
                }
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    break;
                }
            }
        });
    }

    private String toJson(ChatMessage message) {
        // Для простоты: используйте Jackson ObjectMapper в реальной разработке
        return String.format("{\"sender\":\"%s\",\"getter\":\"%s\",\"content\":\"%s\",\"type\":\"%s\"}",
                message.getSender(), message.getGetter(), message.getContent(), message.getType());
    }
}
