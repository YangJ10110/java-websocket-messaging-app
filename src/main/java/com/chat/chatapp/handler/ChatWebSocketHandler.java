package com.chat.chatapp.handler;

import org.springframework.web.socket.*;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ChatWebSocketHandler extends TextWebSocketHandler {

    private final Map<String, WebSocketSession> sessions = new ConcurrentHashMap<>();

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        sessions.put(session.getId(), session);
        session.sendMessage(new TextMessage("Your session ID: " + session.getId()));
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        // Expected message format: "sessionId:message"
        String payload = message.getPayload();
        if (!payload.contains(":")) {
            session.sendMessage(new TextMessage("Invalid format. Use sessionId:message"));
            return;
        }

        String[] parts = payload.split(":", 2);
        String targetSessionId = parts[0].trim();
        String msg = parts[1].trim();

        WebSocketSession targetSession = sessions.get(targetSessionId);
        if (targetSession != null && targetSession.isOpen()) {
            targetSession.sendMessage(new TextMessage("From " + session.getId() + ": " + msg));
        } else {
            session.sendMessage(new TextMessage("Session " + targetSessionId + " not found or closed."));
        }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        sessions.remove(session.getId());
    }
}
