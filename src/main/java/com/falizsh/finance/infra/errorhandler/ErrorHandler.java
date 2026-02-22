package com.falizsh.finance.infra.errorhandler;

import com.falizsh.finance.infra.exception.DuplicatedDataException;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@RestControllerAdvice
public class ErrorHandler {

    private final MessageSource messageSource;

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ProblemDetail> handleEntityNotFoundException(EntityNotFoundException ex) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
                HttpStatus.NOT_FOUND,
                ex.getMessage() != null ? ex.getMessage() : "Resource solicited not found"
        );

        problemDetail.setTitle("Resource Not Found");

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(problemDetail);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ProblemDetail> handleValidationException(MethodArgumentNotValidException ex) {

        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
                HttpStatus.BAD_REQUEST,
                "One or more fields are invalid. Please check the request and try again."
        );

        problemDetail.setTitle("Validation Error");


        List<Map<String, String>> invalidField = ex.getBindingResult().getFieldErrors().stream()
                .map(e -> Map.of(
                        "field", e.getField(),
                        "message", e.getDefaultMessage() != null ? e.getDefaultMessage() : "Invalid value"
                ))
                .toList();

        problemDetail.setProperty("invalidFields", invalidField);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(problemDetail);
    }

    @ExceptionHandler(DuplicatedDataException.class)
    public ResponseEntity<ProblemDetail> handleDuplicatedDataException(DuplicatedDataException ex) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
                HttpStatus.CONFLICT,
                ex.getMessage() != null ? getMessage(ex.getMessage()) : "Data conflict occurred. Please check the request and try again."
        );

        problemDetail.setTitle("Data Conflict");

        return ResponseEntity.status(HttpStatus.CONFLICT).body(problemDetail);
    }

    private String getMessage(String key) {
        return messageSource.getMessage(
                key,
                null,
                null,
                LocaleContextHolder.getLocale()
        );
    }
}
