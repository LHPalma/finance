package com.falizsh.finance.infrastructure.audit.repository;

import com.falizsh.finance.infrastructure.audit.model.ApiAuditLog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;

public interface ApiAuditLogRepository extends JpaRepository<ApiAuditLog, Long> {

    Page<ApiAuditLog> findByUsername(String username, Pageable pageable);

    Page<ApiAuditLog> findByAction(String action, Pageable pageable);

    Page<ApiAuditLog> findByStatus(String status, Pageable pageable);

    Page<ApiAuditLog> findByTimeBetween(LocalDateTime start, LocalDateTime end, Pageable pageable);

    Page<ApiAuditLog> findByUsernameAndTimeBetween(
            String username,
            LocalDateTime start,
            LocalDateTime end,
            Pageable pageable
    );
}
