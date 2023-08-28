package com.example.restapi.configuration;

import com.example.restapi.websocket.MyWebSocketHandler;
import com.example.restapi.websocket.UserIdHandshakeInterceptor;
import jakarta.websocket.server.ServerContainer;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.servlet.server.ServletWebServerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

@Configuration
@RequiredArgsConstructor
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {
    private final MyWebSocketHandler chatHandler;
    private final MyWebSocketHandler broadcastHandler;

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(chatHandler, "/test")
                .addInterceptors(new UserIdHandshakeInterceptor()) // 인터셉터 추가
                .setAllowedOrigins("*");
        registry.addHandler(broadcastHandler, "test/monitoring")
                .addInterceptors(new UserIdHandshakeInterceptor()) // 인터셉터 추가
                .setAllowedOrigins("*");
//        registry.addHandler(chatHandler, "/{login_id}/internal/proc")
//                .addInterceptors(new UserIdHandshakeInterceptor()) // 인터셉터 추가
//                .setAllowedOrigins("*");
//        registry.addHandler(broadcastHandler, "/{login_id}/internal/proc/monitoring")
//                .addInterceptors(new UserIdHandshakeInterceptor()) // 인터셉터 추가
//                .setAllowedOrigins("*");
    }


}
