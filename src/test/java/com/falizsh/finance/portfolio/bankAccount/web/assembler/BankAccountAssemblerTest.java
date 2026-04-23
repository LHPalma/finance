package com.falizsh.finance.portfolio.bankAccount.web.assembler;

import com.falizsh.finance.identity.users.user.model.User;
import com.falizsh.finance.portfolio.bankAccount.application.dto.account.response.BankAccountResponse;
import com.falizsh.finance.portfolio.bankAccount.domain.model.account.BankAccountDetail;
import com.falizsh.finance.portfolio.bankAccount.domain.model.type.SystemAccountType;
import com.falizsh.finance.portfolio.bankAccount.infrastructure.web.account.assembler.BankAccountAssembler;
import com.falizsh.finance.support.TestSupport;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.hateoas.EntityModel;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

class BankAccountAssemblerTest extends TestSupport {

    private BankAccountAssembler assembler;

    @Override
    public void init() {
        assembler = new BankAccountAssembler();
    }

    @Test
    @DisplayName("Should map domain to response and include self link")
    void shouldMapDomainToResponseAndIncludeSelfLink() {
        User user = User.builder().id(1L).name("Luiz").email("luiz@test.com").password("secret").build();
        SystemAccountType type = SystemAccountType.builder().id(2L).name("Checking").allowsOverdraft(true).build();
        BankAccountDetail account = BankAccountDetail.create(
                user,
                "Conta Corrente",
                "Conta principal",
                type,
                BigDecimal.ZERO,
                BigDecimal.valueOf(250.00),
                "USD"
        );

        EntityModel<BankAccountResponse> model = assembler.toModel(account);

        BankAccountResponse response = model.getContent();
        assertThat(response).isNotNull();
        assertThat(response.name()).isEqualTo("Conta Corrente");
        assertThat(response.description()).isEqualTo("Conta principal");
        assertThat(response.type()).isEqualTo("Checking");
        assertThat(response.overdraftLimit()).isEqualByComparingTo("250.00");
        assertThat(response.balance()).isEqualByComparingTo(BigDecimal.ZERO);
        assertThat(response.currency()).isEqualTo("USD");

        assertThat(model.hasLink("self")).isTrue();
    }

    @Test
    @DisplayName("Should apply default values when optional fields are null")
    void shouldApplyDefaultValuesWhenOptionalFieldsAreNull() {
        User user = User.builder().id(1L).name("Luiz").email("luiz@test.com").password("secret").build();
        SystemAccountType type = SystemAccountType.builder().id(2L).name("Savings").build();
        BankAccountDetail account = BankAccountDetail.create(
                user,
                "Conta Reserva",
                null,
                type,
                null,
                null,
                null
        );

        EntityModel<BankAccountResponse> model = assembler.toModel(account);

        BankAccountResponse response = model.getContent();
        assertThat(response.description()).isNull();
        assertThat(response.currency()).isEqualTo("BRL");
        assertThat(response.overdraftLimit()).isEqualByComparingTo(BigDecimal.ZERO);
        assertThat(response.balance()).isEqualByComparingTo(BigDecimal.ZERO);

        assertThat(model.hasLink("self")).isTrue();
    }
}
