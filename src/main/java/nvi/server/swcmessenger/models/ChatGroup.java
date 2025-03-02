package nvi.server.swcmessenger.models;

import lombok.AllArgsConstructor;

import java.util.ArrayList;

@AllArgsConstructor
public class ChatGroup {
    private ArrayList<Client> clients;
}
