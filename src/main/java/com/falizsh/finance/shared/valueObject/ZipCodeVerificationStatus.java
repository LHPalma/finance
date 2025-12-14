package com.falizsh.finance.shared.valueObject;

public enum ZipCodeVerificationStatus {
    NOT_VERIFIED("Não verificado"),
    VERIFIED("Verificado com sucesso"),
    INVALID_FORMAT("Formato inválido"),
    NOT_FOUND("CEP não encontrado"),
    VERIFICATION_FAILED("Falha na verificação externa");

    private final String description;

    ZipCodeVerificationStatus(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
