package com.falizsh.finance.identity.users.userAddress.model;

public enum UserAddressStatus {

    ACTIVE("Ativo"),
    INACTIVE("Inativo"),
    OUTDATED("Desatualizado"),
    INVALID("Inválido");

    private final String displayName;

    UserAddressStatus(String displayName) {
        this.displayName = displayName;
    }

    public static UserAddressStatus fromString(String text) {
        for (UserAddressStatus status : UserAddressStatus.values()) {
            if (status.name().equalsIgnoreCase(text)) {
                return status;
            }
        }
        throw new IllegalArgumentException("invalid.user.address.userStatus");
    }
}
