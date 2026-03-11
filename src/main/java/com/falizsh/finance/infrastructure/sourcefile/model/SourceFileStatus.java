package com.falizsh.finance.infrastructure.sourcefile.model;

public enum SourceFileStatus {

    PENDING("Pendente"),
    PROCESSED("Procesado"),
    FAILED("Falha"),
    PARTIAL_FAILURE("Falha parcial");

    private final String description;

    SourceFileStatus(String description) {
        this.description = description;
    }
}
