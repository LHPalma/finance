package com.falizsh.finance.portfolio.bankAccount.web;

import com.falizsh.finance.identity.users.user.model.User;
import com.falizsh.finance.portfolio.bankAccount.application.dto.account.request.CreateBankAccountRequest;
import com.falizsh.finance.portfolio.bankAccount.application.dto.account.response.BankAccountResponse;
import com.falizsh.finance.portfolio.bankAccount.infrastructure.web.account.BankAccountController;
import com.falizsh.finance.portfolio.bankAccount.domain.model.account.BankAccountDetail;
import com.falizsh.finance.portfolio.bankAccount.domain.model.type.SystemAccountType;
import com.falizsh.finance.portfolio.bankAccount.application.usecase.CreateBankAccountUseCase;
import com.falizsh.finance.portfolio.bankAccount.infrastructure.web.account.assembler.BankAccountAssembler;
import com.falizsh.finance.support.TestSupport;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

class BankAccountControllerTest extends TestSupport {

    @Mock
    private CreateBankAccountUseCase createUseCase;

    @Mock
    private BankAccountAssembler assembler;

    private BankAccountController controller;

    @Override
    public void init() {
        controller = new BankAccountController(createUseCase, assembler);
    }

    @Test
    @DisplayName("Should delegate to use case, pass domain to assembler and return 201")
    void shouldDelegateToUseCasePassDomainToAssemblerAndReturn201() {
        CreateBankAccountRequest request = new CreateBankAccountRequest(
                1L, "Conta Corrente", "Conta principal", 2L, 3L,
                BigDecimal.valueOf(150.00), BigDecimal.valueOf(500.00), "USD"
        );

        BankAccountDetail account = BankAccountDetail.create(
                User.builder().id(1L).name("Luiz").email("luiz@test.com").password("secret").build(),
                "Conta Corrente", "Conta principal",
                SystemAccountType.builder().id(2L).name("Checking").allowsOverdraft(true).build(),
                BigDecimal.valueOf(150.00), BigDecimal.valueOf(500.00), "USD"
        );

        EntityModel<BankAccountResponse> expectedModel = EntityModel.of(
                BankAccountResponse.of()
                        .id(10L).name("Conta Corrente").description("Conta principal")
                        .type("Checking").overdraftLimit(BigDecimal.valueOf(500.00))
                        .balance(BigDecimal.ZERO).currency("USD")
                        .build()
        );

        when(createUseCase.execute(request)).thenReturn(account);
        when(assembler.toModel(account)).thenReturn(expectedModel);

        RequestContextHolder.setRequestAttributes(
                new ServletRequestAttributes(new MockHttpServletRequest("POST", "/bank-accounts"))
        );

        ResponseEntity<EntityModel<BankAccountResponse>> response;
        try {
            response = controller.create(request);
        } finally {
            RequestContextHolder.resetRequestAttributes();
        }

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody()).isEqualTo(expectedModel);

        InOrder verifier = inOrder(createUseCase, assembler);
        verifier.verify(createUseCase).execute(request);
        verifier.verify(assembler).toModel(account);
        verifier.verifyNoMoreInteractions();
    }

    @Test
    @DisplayName("Should propagate exception raised by use case")
    void shouldPropagateExceptionRaisedByUseCase() {
        CreateBankAccountRequest request = new CreateBankAccountRequest(
                1L, "Conta Corrente", null, 2L, null, null, BigDecimal.TEN, "BRL"
        );

        IllegalArgumentException expected = new IllegalArgumentException("invalid bank account");
        when(createUseCase.execute(request)).thenThrow(expected);

        assertThatThrownBy(() -> controller.create(request))
                .isSameAs(expected);

        InOrder verifier = inOrder(createUseCase, assembler);
        verifier.verify(createUseCase).execute(request);
        verifier.verifyNoMoreInteractions();
    }
}
