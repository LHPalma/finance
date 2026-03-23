package com.falizsh.finance.infrastructure.audit.action;

import lombok.Builder;

@Builder
public record AuditContext(
        String username,
        String ip,
        String endpoint,
        String httpMethod,
        String userAgent,
        String requestId,
        String sessionId
) {
}