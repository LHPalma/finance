package com.falizsh.finance.portfolio.bankAccount.web.assembler;

import com.falizsh.finance.portfolio.bankAccount.dto.response.BankAccountResponse;
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
    @DisplayName("Should wrap response into entity model")
    void shouldWrapResponseIntoEntityModel() {
        BankAccountResponse response = BankAccountResponse.builder()
                .id(1L)
                .name("Conta Corrente")
                .description("Conta principal")
                .type("Checking")
                .category("Uso diario")
                .overdraftLimit(BigDecimal.valueOf(100.00))
                .balance(BigDecimal.ZERO)
                .currency("BRL")
                .build();

        EntityModel<BankAccountResponse> model = assembler.toModel(response);

        assertThat(model.getContent()).isEqualTo(response);
        assertThat(model.getLinks()).isEmpty();
    }
}
