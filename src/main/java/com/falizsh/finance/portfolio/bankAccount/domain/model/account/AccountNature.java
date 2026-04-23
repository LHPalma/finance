package com.falizsh.finance.portfolio.bankAccount.domain.model.account;

public enum AccountNature {

        ASSET("Ativos"),
        LIABILITY("Passivos"),
        EQUITY("Patrimônio Líquido"),
        INCOME("Receitas"),
        EXPENSE("Despesas");

        private final String displayName;

        AccountNature(String displayName) {
            this.displayName = displayName;
        }

        public String getDisplayName() {
            return displayName;
        }
}
