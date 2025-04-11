package com.yourpackage.handler;

import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import org.springframework.web.socket.TextMessage;

public class ChatWebSocketHandler extends TextWebSocketHandler {
    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        String clientMessage = message.getPayload();
        session.sendMessage(new TextMessage("Server received: " + clientMessage));
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        System.out.println("✅ WebSocket connection established with session: " + session.getId());
    }
}


