package com.falizsh.finance.users.userTelephone.model;

public enum UserTelephoneStatus {
    ACTIVE("Ativo"),
    INACTIVE("Inativo"),
    UNVERIFIED("Não verificado"),
    OUTDATED("Desatualizado"),
    WRONG_PERSON("Pessoa errada"),
    INVALID("Inválido");

    private final String displayName;

    UserTelephoneStatus(String displayName) {
        this.displayName = displayName;
    }

    public static UserTelephoneStatus fromString(String text) {
        for (UserTelephoneStatus status : UserTelephoneStatus.values()) {
            if (status.name().equalsIgnoreCase(text)) {
                return status;
            }
        }
        throw new IllegalArgumentException("invalid.user.telephone.status");
    }
}
