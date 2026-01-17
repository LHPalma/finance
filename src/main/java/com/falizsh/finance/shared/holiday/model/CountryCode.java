package com.falizsh.finance.shared.holiday.model;

public enum CountryCode {

    BR("Brasil"),
    US("Estados Unidos");
    
    private final String displayName;

    CountryCode(String displayName) {
        this.displayName = displayName;
    }
}
