package com.falizsh.finance.infrastructure.audit.repository.command;

import com.falizsh.finance.infrastructure.audit.model.ApiAuditLog;

public interface ApiAuditLogCommand {

    void save(ApiAuditLog auditLog);

}
