package com.falizsh.finance.userTelephone.model;

public enum TelephoneType {

    PERSONAL("Pessoal"),
    PROFESSIONAL("Profissional"),
    RESIDENTIAL("Residencial"),
    COMMERCIAL("Comercial");

    private final String displayName;

    TelephoneType(String displayName) {
        this.displayName = displayName;
    }

    public static TelephoneType fromString(String text) {
        for (TelephoneType type : TelephoneType.values()) {
            if (type.name().equalsIgnoreCase(text)) {
                return type;
            }
        }
        throw new IllegalArgumentException("invalid.user.telephone.status");
    }

}
