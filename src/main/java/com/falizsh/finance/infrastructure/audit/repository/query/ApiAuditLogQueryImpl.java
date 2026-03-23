package com.falizsh.finance.infrastructure.audit.repository.query;

import com.falizsh.finance.infrastructure.audit.model.ApiAuditLog;
import com.falizsh.finance.infrastructure.audit.repository.ApiAuditLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ApiAuditLogQueryImpl implements ApiAuditLogQuery {

    private final ApiAuditLogRepository repository;

    @Override
    public Page<ApiAuditLog> findAll(Pageable pageable) {
        return repository.findAll(pageable);
    }

    @Override
    public Page<ApiAuditLog> findByUsername(String username, Pageable pageable) {
        return repository.findByUsername(username, pageable);
    }

    @Override
    public Page<ApiAuditLog> findByAction(String action, Pageable pageable) {
        return repository.findByAction(action, pageable);
    }

    @Override
    public Page<ApiAuditLog> findByStatus(String status, Pageable pageable) {
        return repository.findByStatus(status, pageable);
    }

    @Override
    public Page<ApiAuditLog> findByTimeBetween(LocalDateTime startTime, LocalDateTime endTime, Pageable pageable) {
        return repository.findByTimeBetween(startTime, endTime, pageable);
    }

    @Override
    public Page<ApiAuditLog> findByUsernameAndTimeBetween(
            String username,
            LocalDateTime startTime,
            LocalDateTime endTime,
            Pageable pageable
    ) {
        return repository.findByUsernameAndTimeBetween(username, startTime, endTime, pageable);
    }

}
