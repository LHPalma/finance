package com.falizsh.finance.infrastructure.audit.repository.query;

import com.falizsh.finance.infrastructure.audit.model.ApiAuditLog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;

public interface ApiAuditLogQuery {

    Page<ApiAuditLog> findAll(Pageable pageable);

    Page<ApiAuditLog> findByUsername(String username, Pageable pageable);

    Page<ApiAuditLog> findByAction(String action, Pageable pageable);

    Page<ApiAuditLog> findByStatus(String status, Pageable pageable);

    Page<ApiAuditLog> findByTimeBetween(LocalDateTime startTime, LocalDateTime endTime, Pageable pageable);

    Page<ApiAuditLog> findByUsernameAndTimeBetween(
            String username,
            LocalDateTime startTime,
            LocalDateTime endTime,
            Pageable pageable
    );

}
