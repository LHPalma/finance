package com.falizsh.finance.infrastructure.audit.model;

import com.falizsh.finance.infrastructure.audit.converter.InetConverter;
import io.hypersistence.utils.hibernate.type.basic.Inet;
import io.hypersistence.utils.hibernate.type.basic.PostgreSQLInetType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.Type;

import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "ApiAuditLog")
@Table(name = "api_audit_log")
public class ApiAuditLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @CreationTimestamp
    @Column(name = "time", nullable = false)
    private LocalDateTime time;

    @Column(name = "username")
    private String username;

    @Type(PostgreSQLInetType.class)
    @Column(name = "ip", columnDefinition = "inet")
    private Inet ip;

    @Column(name = "action", nullable = false)
    private String action;

    @Column(name = "description")
    private String description;

    @Column(name = "method_name")
    private String methodName;

    @Column(name = "endpoint")
    private String endpoint;

    @Column(name = "http_method")
    private String httpMethod;

    @Column(name = "status", nullable = false)
    private String status;

    @Column(name = "execution_time")
    private Long executionTime;

    @Column(name = "error_message")
    private String errorMessage;

    @Column(name = "user_agent")
    private String userAgent;

    @Column(name = "request_id")
    private String requestId;

    @Column(name = "session_id")
    private String sessionId;

}
