package com.falizsh.finance.economics.model;

public enum VnaStatus {

    F("Fechado"),
    P("Projetado"),
    NI("Não informada");

    private final String displayName;
    VnaStatus(String displayName) {
        this.displayName = displayName;
    }

    public static VnaStatus fromString(String value) {
        if  (value == null ||  value.isBlank()) {
            return F;
        }

        try {
            return VnaStatus.valueOf(value.toUpperCase());
        } catch (IllegalArgumentException e) {
            return F;
        }
    }

}
