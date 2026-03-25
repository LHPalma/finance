package com.falizsh.finance.infrastructure.audit.aspect;

import com.falizsh.finance.infrastructure.audit.action.AuditContext;
import com.falizsh.finance.infrastructure.audit.action.ExtractAuditContextAction;
import com.falizsh.finance.infrastructure.audit.action.SaveAuditLogAction;
import com.falizsh.finance.infrastructure.audit.annotation.Audit;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.lang.reflect.Method;

@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class AuditAspect {

    private final ExtractAuditContextAction extractContext;
    private final SaveAuditLogAction saveAuditLog;

    @Pointcut("within(@org.springframework.web.bind.annotation.RestController *)")
    public void restControllerMethods() {}

    @Around("restControllerMethods()")
    public Object auditMethod(ProceedingJoinPoint joinPoint) throws Throwable {

        AuditContext context = extractContext.extract();

        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        String methodName = method.getName();

        String httpMethod = "UNKNOWN";
        String requestUri = "UNKNOWN_URI";

        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attributes != null) {
            HttpServletRequest request = attributes.getRequest();
            httpMethod = request.getMethod();
            requestUri = request.getRequestURI();
        }

        Audit auditAnnotation = method.getAnnotation(Audit.class);

        String action = auditAnnotation != null ? auditAnnotation.action() : "API_REQUEST";
        String description = auditAnnotation != null && !auditAnnotation.description().isBlank()
                ? auditAnnotation.description()
                : String.format("Endpoint accessed: [%s] %s", httpMethod, requestUri);

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
            long executionTime = System.currentTimeMillis() - startTime;

            saveAuditLog.execute(
                    context,
                    action,
                    description,
                    methodName,
                    status,
                    executionTime,
                    errorMessage
            );
        }
    }
}