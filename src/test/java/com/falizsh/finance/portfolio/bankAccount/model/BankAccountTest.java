package com.falizsh.finance.portfolio.bankAccount.model;

import com.falizsh.finance.identity.users.user.model.User;
import com.falizsh.finance.portfolio.bankAccount.domain.model.account.BankAccount;
import com.falizsh.finance.portfolio.bankAccount.domain.model.type.SystemAccountType;
import com.falizsh.finance.portfolio.bankAccount.domain.model.category.UserAccountCategory;
import com.falizsh.finance.support.TestSupport;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class BankAccountTest extends TestSupport {

    @Override
    public void init() {
    }

    @Test
    @DisplayName("Should create bank account with provided values and defaults")
    void shouldCreateBankAccountWithProvidedValuesAndDefaults() {
        User user = User.builder().id(1L).name("Luiz").email("luiz@test.com").password("secret").build();
        SystemAccountType type = SystemAccountType.builder().id(2L).name("Checking").build();
        UserAccountCategory category = UserAccountCategory.builder().id(3L).user(user).name("Casa").build();

        BankAccount account = BankAccount.create(
                user,
                "Conta Casa",
                "Despesas fixas",
                type,
                category,
                BigDecimal.valueOf(350.00),
                "USD"
        );

        assertThat(account.getUser()).isEqualTo(user);
        assertThat(account.getName()).isEqualTo("Conta Casa");
        assertThat(account.getDescription()).isEqualTo("Despesas fixas");
        assertThat(account.getType()).isEqualTo(type);
        assertThat(account.getCategory()).isEqualTo(category);
        assertThat(account.getBalance()).isEqualByComparingTo(BigDecimal.ZERO);
        assertThat(account.getOverdraftLimit()).isEqualByComparingTo("350.00");
        assertThat(account.getCurrency()).isEqualTo("USD");
        assertThat(account.getIsActive()).isTrue();
    }

    @Test
    @DisplayName("Should fallback to default overdraft and currency when values are missing")
    void shouldFallbackToDefaultOverdraftAndCurrencyWhenValuesAreMissing() {
        User user = User.builder().id(1L).name("Luiz").email("luiz@test.com").password("secret").build();
        SystemAccountType type = SystemAccountType.builder().id(2L).name("Savings").build();

        BankAccount account = BankAccount.create(
                user,
                "Conta Reserva",
                null,
                type,
                null,
                null,
                " "
        );

        assertThat(account.getCategory()).isNull();
        assertThat(account.getDescription()).isNull();
        assertThat(account.getOverdraftLimit()).isEqualByComparingTo(BigDecimal.ZERO);
        assertThat(account.getCurrency()).isEqualTo(BankAccount.DEFAULT_CURRENCY);
        assertThat(account.getBalance()).isEqualByComparingTo(BigDecimal.ZERO);
        assertThat(account.getIsActive()).isTrue();
    }

    @Test
    @DisplayName("Should reject creation when mandatory data is invalid")
    void shouldRejectCreationWhenMandatoryDataIsInvalid() {
        SystemAccountType type = SystemAccountType.builder().id(2L).name("Checking").build();
        User user = User.builder().id(1L).name("Luiz").email("luiz@test.com").password("secret").build();

        assertThatThrownBy(() -> BankAccount.create(null, "Conta", null, type, null, null, null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("User, Type and Name are mandatory to create a Bank Account.");

        assertThatThrownBy(() -> BankAccount.create(user, " ", null, type, null, null, null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("User, Type and Name are mandatory to create a Bank Account.");

        assertThatThrownBy(() -> BankAccount.create(user, "Conta", null, null, null, null, null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("User, Type and Name are mandatory to create a Bank Account.");
    }
}
