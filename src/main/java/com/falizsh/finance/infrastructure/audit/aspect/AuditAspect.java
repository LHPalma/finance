package com.falizsh.finance.infrastructure.audit.aspect;

import com.falizsh.finance.infrastructure.audit.action.AuditContext;
import com.falizsh.finance.infrastructure.audit.action.ExtractAuditContextAction;
import com.falizsh.finance.infrastructure.audit.action.SaveAuditLogAction;
import com.falizsh.finance.infrastructure.audit.annotation.Audit;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class AuditAspect {

    private final ExtractAuditContextAction extractContext;
    private final SaveAuditLogAction saveAuditLog;

    @Around("@annotation(audit)")
    public Object auditMethod(ProceedingJoinPoint joinPoint, Audit audit) throws Throwable {

        AuditContext context = extractContext.extract();

        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        String methodName = signature.getMethod().getName();

        long startTime = System.currentTimeMillis();

        String status = "SUCCESS";
        String errorMessage = null;

        try {
            return joinPoint.proceed();

        } catch (Throwable ex) {
            status = "ERROR";
            errorMessage = ex.getMessage() != null ? ex.getMessage() : ex.getClass().getSimpleName();
            throw ex;

        } finally {

            long executionTime = calculateExecutionTime(startTime);

            saveAuditLog.execute(
                    context,
                    audit.action(),
                    audit.description(),
                    methodName,
                    status,
                    executionTime,
                    errorMessage
            );
        }
    }

    private long calculateExecutionTime(long startTime) {
        return System.currentTimeMillis() - startTime;
    }
}