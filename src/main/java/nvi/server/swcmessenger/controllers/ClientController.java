package nvi.server.swcmessenger.controllers;

import lombok.RequiredArgsConstructor;
import nvi.server.swcmessenger.databases.ClientRepository;
import nvi.server.swcmessenger.models.Client;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ClientController {

    private final ClientRepository clientRepository;
//    private final BCryptPasswordEncoder passwordEncoder;

    /**
     * Проверяет существование клиента по email
     * @param email email клиента
     * @return true если клиент существует, иначе false
     */
    public boolean clientExists(String email) {
        return clientRepository.findByEmail(email).isPresent();
    }

    /**
     * Аутентифицирует клиента по email и телефону
     * @param email email клиента
     * @param phone телефон клиента
     * @return найденный клиент или null
     */
    /**
     * Проверяет авторизацию пользователя по email и паролю
     * @param email email пользователя
     * @param password пароль пользователя в открытом виде
     * @return объект Client в случае успешной авторизации или null
     */
//    public Client authenticateClient(String email, String password) {
//        Optional<Client> clientOptional = clientRepository.findByEmail(email);
//
//        if (clientOptional.isPresent()) {
//            Client client = clientOptional.get();
//            if (passwordEncoder.matches(password, client.getPasswordHash())) {
//                return client;
//            }
//        }
//
//        return null;
//    }

    /**
     * Создает нового клиента
     * @param client данные клиента
     * @return сохраненный клиент
     */
    public Client createClient(Client client) {
        if (clientExists(client.getEmail())) {
            throw new IllegalArgumentException("Клиент с таким email уже существует");
        }
        return clientRepository.save(client);
    }

    /**
     * Хеширует пароль при регистрации пользователя
     * @param password пароль в открытом виде
     * @return хеш пароля
     */
//    public String hashPassword(String password) {
//        return passwordEncoder.encode(password);
//    }
}
