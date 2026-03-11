package com.falizsh.finance.marketdata.holiday.model;

public enum CountryCode {

    BR("Brasil"),
    US("Estados Unidos");
    
    private final String displayName;

    CountryCode(String displayName) {
        this.displayName = displayName;
    }
}
