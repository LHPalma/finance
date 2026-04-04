package com.falizsh.finance.portfolio.bankAccount.mapper;

import com.falizsh.finance.identity.users.user.model.User;
import com.falizsh.finance.portfolio.bankAccount.dto.response.BankAccountResponse;
import com.falizsh.finance.portfolio.bankAccount.model.BankAccount;
import com.falizsh.finance.portfolio.bankAccount.systemAccountType.model.SystemAccountType;
import com.falizsh.finance.portfolio.bankAccount.userAccountCategory.model.UserAccountCategory;
import com.falizsh.finance.support.TestSupport;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

class BankAccountMapperTest extends TestSupport {

    private BankAccountMapper mapper;

    @Override
    public void init() {
        mapper = new BankAccountMapper();
    }

    @Test
    @DisplayName("Should map bank account to response")
    void shouldMapBankAccountToResponse() {
        User user = User.builder().id(1L).name("Luiz").email("luiz@test.com").password("secret").build();
        SystemAccountType type = SystemAccountType.builder().id(2L).name("Checking").build();
        UserAccountCategory category = UserAccountCategory.builder().id(3L).user(user).name("Uso diario").build();
        BankAccount account = BankAccount.create(
                user,
                "Conta Corrente",
                "Conta principal",
                type,
                category,
                BigDecimal.valueOf(250.00),
                "USD"
        );

        BankAccountResponse response = mapper.toResponse(account);

        assertThat(response.id()).isNull();
        assertThat(response.name()).isEqualTo("Conta Corrente");
        assertThat(response.description()).isEqualTo("Conta principal");
        assertThat(response.type()).isEqualTo("Checking");
        assertThat(response.category()).isEqualTo("Uso diario");
        assertThat(response.overdraftLimit()).isEqualByComparingTo("250.00");
        assertThat(response.balance()).isEqualByComparingTo(BigDecimal.ZERO);
        assertThat(response.currency()).isEqualTo("USD");
    }

    @Test
    @DisplayName("Should map bank account without category")
    void shouldMapBankAccountWithoutCategory() {
        User user = User.builder().id(1L).name("Luiz").email("luiz@test.com").password("secret").build();
        SystemAccountType type = SystemAccountType.builder().id(2L).name("Savings").build();
        BankAccount account = BankAccount.create(
                user,
                "Conta Reserva",
                null,
                type,
                null,
                null,
                null
        );

        BankAccountResponse response = mapper.toResponse(account);

        assertThat(response.category()).isNull();
        assertThat(response.currency()).isEqualTo("BRL");
        assertThat(response.overdraftLimit()).isEqualByComparingTo(BigDecimal.ZERO);
    }
}
