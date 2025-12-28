package com.falizsh.finance.bankAccount.model;

public enum AccountType {

    CHECKING("Conta Corrente"),
    SAVINGS("Conta Poupança"),
    INVESTIMENTS("Conta de Investimentos"),
    CASH("Caixa Físico");

    private final String displayName;

    AccountType(String displayName) {
        this.displayName = displayName;
    }

    public static AccountType fromString(String text) {
        for (AccountType type : AccountType.values()) {
            if (type.name().equalsIgnoreCase(text)) {
                return type;
            }
        }
        throw new IllegalArgumentException("invalid.account.type");
    }

}