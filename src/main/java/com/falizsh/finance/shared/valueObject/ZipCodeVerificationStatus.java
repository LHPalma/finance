package com.falizsh.finance.shared.valueObject;

import lombok.Getter;

@Getter
public enum ZipCodeVerificationStatus {
    NOT_VERIFIED("Não verificado"),
    VERIFIED("Verificado com sucesso"),
    INVALID_FORMAT("Formato inválido"),
    NOT_FOUND("CEP não encontrado"),
    VERIFICATION_FAILED("Falha na verificação externa");

    private final String displayName;

    ZipCodeVerificationStatus(String displayName) {
        this.displayName = displayName;
    }

    public static ZipCodeVerificationStatus fromString(String text) {
        for (ZipCodeVerificationStatus status : ZipCodeVerificationStatus.values()) {
            if (status.name().equalsIgnoreCase(text)) {
                return status;
            }
        }
        throw new IllegalArgumentException("invalid.zipCodeVerification.status");
    }

}
