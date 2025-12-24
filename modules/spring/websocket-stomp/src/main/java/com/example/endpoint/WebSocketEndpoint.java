package com.example.endpoint;

import jakarta.websocket.*;
import jakarta.websocket.server.ServerEndpoint;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

@Slf4j
@ServerEndpoint(value = "/ws", configurator = WebSocketConfigurator.class)
public class WebSocketEndpoint {

    private static final List<Session> sessions = new CopyOnWriteArrayList<>();

    @OnOpen
    public void onOpen(Session session, EndpointConfig config) {
        sessions.add(session);
        
        log.info("=== WebSocket 연결 수립 ===");
        log.info("세션 ID: {}", session.getId());
        
        // HTTP 요청 헤더 로깅
        Map<String, List<String>> requestHeaders = (Map<String, List<String>>) 
            config.getUserProperties().get("requestHeaders");
        
        if (requestHeaders != null) {
            log.info("=== HTTP Upgrade 요청 헤더 ===");
            requestHeaders.forEach((key, values) -> 
                log.info("  {}: {}", key, String.join(", ", values))
            );
        }
        
        // HTTP 요청 파라미터 로깅
        Map<String, List<String>> requestParameters = session.getRequestParameterMap();
        if (!requestParameters.isEmpty()) {
            log.info("=== HTTP 요청 파라미터 ===");
            requestParameters.forEach((key, values) -> 
                log.info("  {}: {}", key, String.join(", ", values))
            );
        }
        
        // WebSocket 프로토콜 정보
        log.info("=== WebSocket 프로토콜 정보 ===");
        log.info("프로토콜: {}", session.getProtocolVersion());
        log.info("서브프로토콜: {}", session.getNegotiatedSubprotocol());
        log.info("확장: {}", session.getNegotiatedExtensions());
        
        log.info("=== WebSocket 연결 완료 ===");
    }

    @OnMessage
    public void onMessage(Session session, String message) {
        log.info("메시지 수신 [세션: {}]: {}", session.getId(), message);
        
        try {
            // 에코 응답
            session.getBasicRemote().sendText("Echo: " + message);
        } catch (IOException e) {
            log.error("메시지 전송 실패", e);
        }
    }

    @OnClose
    public void onClose(Session session, CloseReason closeReason) {
        sessions.remove(session);
        log.info("=== WebSocket 연결 종료 ===");
        log.info("세션 ID: {}", session.getId());
        log.info("종료 코드: {}", closeReason.getCloseCode());
        log.info("종료 사유: {}", closeReason.getReasonPhrase());
    }

    @OnError
    public void onError(Session session, Throwable error) {
        log.error("WebSocket 오류 발생 [세션: {}]", session.getId(), error);
    }
}

