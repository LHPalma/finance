package com.falizsh.finance.portfolio.bankAccount.usecase;

import com.falizsh.finance.identity.users.user.model.User;
import com.falizsh.finance.portfolio.bankAccount.dto.request.CreateBankAccountRequest;
import com.falizsh.finance.portfolio.bankAccount.model.BankAccount;
import com.falizsh.finance.portfolio.bankAccount.repository.command.CreateBankAccountCommand;
import com.falizsh.finance.portfolio.bankAccount.repository.command.CreateBankAccountHandler;
import com.falizsh.finance.portfolio.bankAccount.systemAccountType.model.SystemAccountType;
import com.falizsh.finance.portfolio.bankAccount.userAccountCategory.model.UserAccountCategory;
import com.falizsh.finance.support.TestSupport;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class CreateBankAccountUseCaseTest extends TestSupport {

    @Mock
    private CreateBankAccountHandler handler;

    private CreateBankAccount useCase;

    @Override
    public void init() {
        useCase = new CreateBankAccount(handler);
    }

    @Test
    @DisplayName("Should map request to command and return created bank account")
    void shouldMapRequestToCommandAndReturnCreatedBankAccount() {
        CreateBankAccountRequest request = new CreateBankAccountRequest(
                1L,
                "Conta Principal",
                "Minha conta principal",
                2L,
                3L,
                BigDecimal.valueOf(1000.00),
                BigDecimal.valueOf(500.00),
                "USD"
        );

        User user = User.builder()
                .id(1L)
                .name("Luiz")
                .email("luiz@test.com")
                .password("secret")
                .build();

        SystemAccountType type = SystemAccountType.builder()
                .id(2L)
                .name("Checking")
                .allowsOverdraft(true)
                .build();

        UserAccountCategory category = UserAccountCategory.builder()
                .id(3L)
                .user(user)
                .name("Uso diario")
                .build();

        BankAccount expectedAccount = BankAccount.create(
                user,
                "Conta Principal",
                "Minha conta principal",
                type,
                category,
                BigDecimal.valueOf(500.00),
                "USD"
        );

        when(handler.handle(any(CreateBankAccountCommand.class))).thenReturn(expectedAccount);

        BankAccount result = useCase.execute(request);

        ArgumentCaptor<CreateBankAccountCommand> commandCaptor = ArgumentCaptor.forClass(CreateBankAccountCommand.class);
        verify(handler).handle(commandCaptor.capture());

        CreateBankAccountCommand command = commandCaptor.getValue();
        assertThat(command.userId()).isEqualTo(1L);
        assertThat(command.name()).isEqualTo("Conta Principal");
        assertThat(command.description()).isEqualTo("Minha conta principal");
        assertThat(command.systemTypeId()).isEqualTo(2L);
        assertThat(command.userCategoryId()).isEqualTo(3L);
        assertThat(command.initialBalance()).isEqualByComparingTo("1000.00");
        assertThat(command.overdraftLimit()).isEqualByComparingTo("500.00");
        assertThat(command.currency()).isEqualTo("USD");

        assertThat(result).isSameAs(expectedAccount);
    }

    @Test
    @DisplayName("Should allow optional fields to be null")
    void shouldAllowOptionalFieldsToBeNull() {
        CreateBankAccountRequest request = new CreateBankAccountRequest(
                1L,
                "Conta Simples",
                null,
                2L,
                null,
                null,
                null,
                null
        );

        User user = User.builder()
                .id(1L)
                .name("Luiz")
                .email("luiz@test.com")
                .password("secret")
                .build();

        SystemAccountType type = SystemAccountType.builder()
                .id(2L)
                .name("Savings")
                .build();

        BankAccount expectedAccount = BankAccount.create(
                user,
                "Conta Simples",
                null,
                type,
                null,
                null,
                null
        );

        when(handler.handle(any(CreateBankAccountCommand.class))).thenReturn(expectedAccount);

        BankAccount result = useCase.execute(request);

        ArgumentCaptor<CreateBankAccountCommand> commandCaptor = ArgumentCaptor.forClass(CreateBankAccountCommand.class);
        verify(handler).handle(commandCaptor.capture());

        CreateBankAccountCommand command = commandCaptor.getValue();
        assertThat(command.userId()).isEqualTo(1L);
        assertThat(command.name()).isEqualTo("Conta Simples");
        assertThat(command.description()).isNull();
        assertThat(command.systemTypeId()).isEqualTo(2L);
        assertThat(command.userCategoryId()).isNull();
        assertThat(command.initialBalance()).isNull();
        assertThat(command.overdraftLimit()).isNull();
        assertThat(command.currency()).isNull();

        assertThat(result).isSameAs(expectedAccount);
    }
}
