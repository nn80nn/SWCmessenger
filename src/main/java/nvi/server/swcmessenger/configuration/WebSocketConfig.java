package nvi.server.swcmessenger.configuration;

import lombok.RequiredArgsConstructor;
import nvi.server.swcmessenger.websocket.AuthWebSocketHandler;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.*;

@Configuration
@EnableWebSocket
@RequiredArgsConstructor
public class WebSocketConfig implements WebSocketConfigurer {

    private final AuthWebSocketHandler authWebSocketHandler;

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(authWebSocketHandler, "/ws")
                .setAllowedOrigins("*");
    }
}
