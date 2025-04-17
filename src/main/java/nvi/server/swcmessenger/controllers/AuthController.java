package nvi.server.swcmessenger.controllers;

import lombok.RequiredArgsConstructor;
import nvi.server.swcmessenger.models.Client;
import nvi.server.swcmessenger.services.ClientService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final ClientService clientService;

    @PostMapping("/register")
    public Client register(@RequestBody Client client, @RequestParam String password) {
        return clientService.createClient(client, password);
    }

    @PostMapping("/login")
    public String login(@RequestParam String email, @RequestParam String password) {
        Client client = clientService.authenticateClient(email, password);
        if (client != null) {
            // Возвращаем "токен" — для простоты email, но лучше JWT
            return client.getEmail();
        }
        throw new RuntimeException("Неверный логин или пароль");
    }
}
