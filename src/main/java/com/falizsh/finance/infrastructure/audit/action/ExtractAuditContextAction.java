package com.falizsh.finance.infrastructure.audit.action;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.UUID;

@Slf4j
@Component
public class ExtractAuditContextAction {

    public AuditContext extract() {
        try {
            HttpServletRequest request = getCurrentRequest();

            if (request == null) {
                return createDefaultContext();
            }

            String username = extractUsername();
            String ip = extractIpAddress(request);
            String endpoint = request.getRequestURI();
            String httpMethod = request.getMethod();
            String userAgent = request.getHeader("User-Agent");
            String requestId = extractRequestId(request);
            String sessionId = extractSessionId(request);

            return new AuditContext(
                    username,
                    ip,
                    endpoint,
                    httpMethod,
                    userAgent,
                    requestId,
                    sessionId
            );

        } catch (Exception e) {
            log.warn("Erro ao extrair contexto de auditoria: {}", e.getMessage());
            return createDefaultContext();
        }
    }

    private HttpServletRequest getCurrentRequest() {
        ServletRequestAttributes attributes =
                (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();

        return attributes != null ? attributes.getRequest() : null;
    }

    private String extractUsername() {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

            if (authentication == null || !authentication.isAuthenticated()) {
                return "ANONYMOUS";
            }

            String username = authentication.getName();

            return "anonymousUser".equals(username) ? "ANONYMOUS" : username;

        } catch (Exception e) {
            return "ANONYMOUS";
        }
    }

    private String extractIpAddress(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");

        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("X-Real-IP");
        }

        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }

        if (ip != null && ip.contains(",")) {
            ip = ip.split(",")[0].trim();
        }

        return ip != null ? ip : "UNKNOWN";
    }

    private String extractRequestId(HttpServletRequest request) {
        String requestId = request.getHeader("X-Request-ID");

        if (requestId == null || requestId.isEmpty()) {
            requestId = UUID.randomUUID().toString();
        }

        return requestId;
    }

    private String extractSessionId(HttpServletRequest request) {
        try {
            HttpSession session = request.getSession(false);
            return session != null ? session.getId() : null;
        } catch (Exception e) {
            return null;
        }
    }

    private AuditContext createDefaultContext() {
        return new AuditContext(
                "UNKNOWN",
                "UNKNOWN",
                "UNKNOWN",
                "UNKNOWN",
                "UNKNOWN",
                UUID.randomUUID().toString(),
                null
        );
    }
}
