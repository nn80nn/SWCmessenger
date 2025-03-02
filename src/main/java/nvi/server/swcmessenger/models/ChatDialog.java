package nvi.server.swcmessenger.models;

import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;

import java.util.ArrayList;

@AllArgsConstructor
public class ChatDialog extends ChatRoom {
    private final Client client1;
    private final Client client2;
}
