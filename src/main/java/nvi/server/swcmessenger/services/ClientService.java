package nvi.server.swcmessenger.services;

import lombok.RequiredArgsConstructor;
import nvi.server.swcmessenger.databases.ClientRepository;
import nvi.server.swcmessenger.models.Client;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ClientService {

    private final ClientRepository clientRepository;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public boolean clientExists(String email) {
        return clientRepository.findByEmail(email).isPresent();
    }

    public Client authenticateClient(String email, String password) {
        Optional<Client> clientOptional = clientRepository.findByEmail(email);
        if (clientOptional.isPresent()) {
            Client client = clientOptional.get();
            if (passwordEncoder.matches(password, client.getPasswordHash())) {
                return client;
            }
        }
        return null;
    }

    public Client createClient(Client client, String rawPassword) {
        if (clientExists(client.getEmail())) {
            throw new IllegalArgumentException("Клиент с таким email уже существует");
        }
        client.setPasswordHash(passwordEncoder.encode(rawPassword));
        return clientRepository.save(client);
    }

    // Для примера: "токен" — это email, в реальном проекте JWT!
    public String getEmailFromToken(String token) {
        if (clientExists(token)) return token;
        return null;
    }
}
