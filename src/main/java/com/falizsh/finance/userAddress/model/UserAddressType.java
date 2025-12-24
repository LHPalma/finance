package com.falizsh.finance.userAddress.model;

public enum UserAddressType {

    RESIDENTIAL("Residencial"),
    COMMERCIAL("Comercial"),
    PROFESSIONAL("Profissinal");

    private final String displayName;

    UserAddressType(String displayName) {
        this.displayName = displayName;
    }

    public static UserAddressType fromString(String text) {
        for (UserAddressType type : UserAddressType.values()) {
            if (type.name().equalsIgnoreCase(text)) {
                return type;
            }
        }
        throw new IllegalArgumentException("invalid.user.address.type");
    }

}
