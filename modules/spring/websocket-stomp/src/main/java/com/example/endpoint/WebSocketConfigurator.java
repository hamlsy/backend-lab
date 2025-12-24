package com.example.endpoint;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.websocket.HandshakeResponse;
import jakarta.websocket.server.HandshakeRequest;
import jakarta.websocket.server.ServerEndpointConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
public class WebSocketConfigurator extends ServerEndpointConfig.Configurator {

    @Override
    public void modifyHandshake(ServerEndpointConfig config,
                                HandshakeRequest request,
                                HandshakeResponse response) {
        
        log.info("=== WebSocket Handshake 시작 ===");
        
        // 요청 헤더 로깅
        Map<String, List<String>> requestHeaders = request.getHeaders();
        log.info("=== HTTP Upgrade 요청 헤더 (HandshakeRequest) ===");
        requestHeaders.forEach((key, values) -> 
            log.info("  {}: {}", key, String.join(", ", values))
        );
        
        // 응답 헤더 로깅
        Map<String, List<String>> responseHeaders = response.getHeaders();
        log.info("=== HTTP Upgrade 응답 헤더 (HandshakeResponse) ===");
        responseHeaders.forEach((key, values) -> 
            log.info("  {}: {}", key, String.join(", ", values))
        );
        
        // HTTP 요청 정보를 UserProperties에 저장 (OnOpen에서 사용)
        Map<String, List<String>> headersCopy = new HashMap<>(requestHeaders);
        config.getUserProperties().put("requestHeaders", headersCopy);
        
        // HTTP 요청 URI 정보
        log.info("=== HTTP 요청 정보 ===");
        log.info("요청 URI: {}", request.getRequestURI());
        log.info("쿼리 문자열: {}", request.getQueryString());
        
        // Spring의 HttpServletRequest에서 추가 정보 가져오기
        try {
            ServletRequestAttributes attributes = 
                (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            if (attributes != null) {
                HttpServletRequest httpRequest = attributes.getRequest();
                log.info("=== HTTP ServletRequest 정보 ===");
                log.info("요청 메서드: {}", httpRequest.getMethod());
                log.info("요청 URL: {}", httpRequest.getRequestURL());
                log.info("프로토콜: {}", httpRequest.getProtocol());
                log.info("원격 주소: {}", httpRequest.getRemoteAddr());
                log.info("원격 포트: {}", httpRequest.getRemotePort());
                
                // 모든 요청 헤더 상세 로깅
                log.info("=== HTTP ServletRequest 헤더 (상세) ===");
                Enumeration<String> headerNames = httpRequest.getHeaderNames();
                while (headerNames.hasMoreElements()) {
                    String headerName = headerNames.nextElement();
                    Enumeration<String> headerValues = httpRequest.getHeaders(headerName);
                    StringBuilder values = new StringBuilder();
                    while (headerValues.hasMoreElements()) {
                        if (values.length() > 0) values.append(", ");
                        values.append(headerValues.nextElement());
                    }
                    log.info("  {}: {}", headerName, values);
                }
            }
        } catch (Exception e) {
            log.warn("ServletRequest 정보를 가져올 수 없습니다", e);
        }
        
        log.info("=== WebSocket Handshake 완료 ===");
    }
}

