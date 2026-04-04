package com.falizsh.finance.portfolio.bankAccount.web;

import com.falizsh.finance.portfolio.bankAccount.dto.request.CreateBankAccountRequest;
import com.falizsh.finance.portfolio.bankAccount.dto.response.BankAccountResponse;
import com.falizsh.finance.portfolio.bankAccount.mapper.BankAccountMapper;
import com.falizsh.finance.portfolio.bankAccount.model.BankAccount;
import com.falizsh.finance.portfolio.bankAccount.systemAccountType.model.SystemAccountType;
import com.falizsh.finance.portfolio.bankAccount.usecase.CreateBankAccountUseCase;
import com.falizsh.finance.portfolio.bankAccount.web.assembler.BankAccountAssembler;
import com.falizsh.finance.support.TestSupport;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class BankAccountControllerTest extends TestSupport {

    @Mock
    private CreateBankAccountUseCase createUseCase;

    @Mock
    private BankAccountMapper bankAccountMapper;

    @Mock
    private BankAccountAssembler bankAccountAssembler;

    private BankAccountController bankAccountController;

    @Override
    public void init() {
        bankAccountController = new BankAccountController(createUseCase, bankAccountMapper, bankAccountAssembler);
    }

    @Test
    @DisplayName("Should delegate request to use case and return created response")
    void shouldDelegateRequestToUseCaseAndReturnCreatedResponse() {
        CreateBankAccountRequest request = new CreateBankAccountRequest(
                1L,
                "Conta Corrente",
                "Conta principal",
                2L,
                3L,
                BigDecimal.valueOf(150.00),
                BigDecimal.valueOf(500.00),
                "USD"
        );

        BankAccount account = BankAccount.create(
                com.falizsh.finance.identity.users.user.model.User.builder()
                        .id(1L)
                        .name("Luiz")
                        .email("luiz@test.com")
                        .password("secret")
                        .build(),
                "Conta Corrente",
                "Conta principal",
                SystemAccountType.builder().id(2L).name("Checking").build(),
                null,
                BigDecimal.valueOf(500.00),
                "USD"
        );

        BankAccountResponse expectedResponse = BankAccountResponse.builder()
                .id(10L)
                .name("Conta Corrente")
                .description("Conta principal")
                .type("Checking")
                .category("Uso diario")
                .overdraftLimit(BigDecimal.valueOf(500.00))
                .balance(BigDecimal.ZERO)
                .currency("USD")
                .build();

        EntityModel<BankAccountResponse> expectedModel = EntityModel.of(expectedResponse);

        when(createUseCase.execute(request)).thenReturn(account);
        when(bankAccountMapper.toResponse(account)).thenReturn(expectedResponse);
        when(bankAccountAssembler.toModel(expectedResponse)).thenReturn(expectedModel);

        ResponseEntity<EntityModel<BankAccountResponse>> response = bankAccountController.create(request);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody()).isEqualTo(expectedModel);

        InOrder verifier = inOrder(createUseCase, bankAccountMapper, bankAccountAssembler);
        verifier.verify(createUseCase).execute(request);
        verifier.verify(bankAccountMapper).toResponse(account);
        verifier.verify(bankAccountAssembler).toModel(expectedResponse);
        verifier.verifyNoMoreInteractions();
    }

    @Test
    @DisplayName("Should propagate exception raised by use case")
    void shouldPropagateExceptionRaisedByUseCase() {
        CreateBankAccountRequest request = new CreateBankAccountRequest(
                1L,
                "Conta Corrente",
                null,
                2L,
                null,
                null,
                BigDecimal.TEN,
                "BRL"
        );

        IllegalArgumentException expected = new IllegalArgumentException("invalid bank account");

        when(createUseCase.execute(request)).thenThrow(expected);

        assertThatThrownBy(() -> bankAccountController.create(request))
                .isSameAs(expected);

        InOrder verifier = inOrder(createUseCase, bankAccountMapper, bankAccountAssembler);
        verifier.verify(createUseCase).execute(request);
        verifier.verifyNoMoreInteractions();
    }
}
