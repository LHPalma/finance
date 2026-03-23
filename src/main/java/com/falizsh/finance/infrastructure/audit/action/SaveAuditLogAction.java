package com.falizsh.finance.infrastructure.audit.action;

import com.falizsh.finance.infrastructure.audit.model.ApiAuditLog;
import com.falizsh.finance.infrastructure.audit.repository.command.ApiAuditLogCommand;
import io.hypersistence.utils.hibernate.type.basic.Inet;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class SaveAuditLogAction {

    private final ApiAuditLogCommand command;

    @Async
    public void execute(
            AuditContext context,
            String action,
            String description,
            String methodName,
            String status,
            Long executionTime,
            String errorMessage
    ) {
        try {
            ApiAuditLog auditLog = ApiAuditLog.builder()
                    .username(context.username())
                    .ip(context.ip() != null ? new Inet(context.ip()) : null)
                    .action(action)
                    .description(description)
                    .methodName(methodName)
                    .endpoint(context.endpoint())
                    .httpMethod(context.httpMethod())
                    .status(status)
                    .executionTime(executionTime)
                    .errorMessage(errorMessage)
                    .userAgent(context.userAgent())
                    .requestId(context.requestId())
                    .sessionId(context.sessionId())
                    .build();

            command.save(auditLog);

        } catch (Exception e) {
            log.error("Falha ao salvar log de auditoria: action={}, user={}, error={}",
                    action, context.username(), e.getMessage(), e);
        }
    }
}
