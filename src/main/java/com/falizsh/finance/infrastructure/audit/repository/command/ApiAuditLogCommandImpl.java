package com.falizsh.finance.infrastructure.audit.repository;

import com.falizsh.finance.infrastructure.audit.model.ApiAuditLog;
import com.falizsh.finance.infrastructure.audit.repository.command.ApiAuditLogCommand;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(rollbackFor = Exception.class)
public class ApiAuditLogCommandImpl implements ApiAuditLogCommand {

    private final ApiAuditLogRepository repository;

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void save(ApiAuditLog auditLog) {
        repository.save(auditLog);
        log.debug("Audit log saved: id={}, action={}, user={}",
                auditLog.getId(), auditLog.getAction(), auditLog.getUsername());
    }
}