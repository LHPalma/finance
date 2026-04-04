package com.falizsh.finance.portfolio.bankAccount.repository.command;

import com.falizsh.finance.identity.users.user.model.User;
import com.falizsh.finance.identity.users.user.repository.UserRepository;
import com.falizsh.finance.infrastructure.exception.BusinessException;
import com.falizsh.finance.portfolio.bankAccount.model.BankAccount;
import com.falizsh.finance.portfolio.bankAccount.repository.BankAccountRepository;
import com.falizsh.finance.portfolio.bankAccount.systemAccountType.model.SystemAccountType;
import com.falizsh.finance.portfolio.bankAccount.systemAccountType.repository.SystemAccountTypeRepository;
import com.falizsh.finance.portfolio.bankAccount.userAccountCategory.model.UserAccountCategory;
import com.falizsh.finance.portfolio.bankAccount.userAccountCategory.repository.UserAccountCategoryRepository;
import com.falizsh.finance.support.TestSupport;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InOrder;
import org.mockito.Mock;

import java.math.BigDecimal;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

class CreateBankAccountHandlerTest extends TestSupport {

    @Mock
    private UserRepository userRepository;

    @Mock
    private SystemAccountTypeRepository systemTypeRepository;

    @Mock
    private UserAccountCategoryRepository categoryRepository;

    @Mock
    private BankAccountRepository accountRepository;

    private CreateBankAccountHandler handler;

    @Override
    public void init() {
        handler = new CreateBankAccountHandler(
                userRepository,
                systemTypeRepository,
                categoryRepository,
                accountRepository
        );
    }

    @Test
    @DisplayName("Should create and persist bank account when command is valid")
    void shouldCreateAndPersistBankAccountWhenCommandIsValid() {
        CreateBankAccountCommand command = new CreateBankAccountCommand(
                1L,
                "Conta Corrente",
                "Conta principal",
                2L,
                3L,
                BigDecimal.valueOf(999.99),
                BigDecimal.valueOf(250.00),
                "USD"
        );

        User user = User.builder().id(1L).name("Luiz").email("luiz@test.com").password("secret").build();
        SystemAccountType type = SystemAccountType.builder().id(2L).name("Checking").allowsOverdraft(true).build();
        UserAccountCategory category = UserAccountCategory.builder().id(3L).user(user).name("Uso diario").build();

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(systemTypeRepository.findById(2L)).thenReturn(Optional.of(type));
        when(categoryRepository.findById(3L)).thenReturn(Optional.of(category));

        BankAccount result = handler.handle(command);

        ArgumentCaptor<BankAccount> accountCaptor = ArgumentCaptor.forClass(BankAccount.class);
        verify(accountRepository).save(accountCaptor.capture());

        BankAccount savedAccount = accountCaptor.getValue();
        assertThat(result).isSameAs(savedAccount);
        assertThat(savedAccount.getUser()).isEqualTo(user);
        assertThat(savedAccount.getType()).isEqualTo(type);
        assertThat(savedAccount.getCategory()).isEqualTo(category);
        assertThat(savedAccount.getName()).isEqualTo("Conta Corrente");
        assertThat(savedAccount.getDescription()).isEqualTo("Conta principal");
        assertThat(savedAccount.getBalance()).isEqualByComparingTo(BigDecimal.ZERO);
        assertThat(savedAccount.getOverdraftLimit()).isEqualByComparingTo("250.00");
        assertThat(savedAccount.getCurrency()).isEqualTo("USD");

        InOrder verifier = inOrder(userRepository, systemTypeRepository, categoryRepository, accountRepository);
        verifier.verify(userRepository).findById(1L);
        verifier.verify(systemTypeRepository).findById(2L);
        verifier.verify(categoryRepository).findById(3L);
        verifier.verify(accountRepository).save(savedAccount);
        verifier.verifyNoMoreInteractions();
    }

    @Test
    @DisplayName("Should create account without category when category id is null")
    void shouldCreateAccountWithoutCategoryWhenCategoryIdIsNull() {
        CreateBankAccountCommand command = new CreateBankAccountCommand(
                1L,
                "Conta Reserva",
                null,
                2L,
                null,
                null,
                null,
                null
        );

        User user = User.builder().id(1L).name("Luiz").email("luiz@test.com").password("secret").build();
        SystemAccountType type = SystemAccountType.builder().id(2L).name("Savings").allowsOverdraft(false).build();

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(systemTypeRepository.findById(2L)).thenReturn(Optional.of(type));

        BankAccount result = handler.handle(command);

        ArgumentCaptor<BankAccount> accountCaptor = ArgumentCaptor.forClass(BankAccount.class);
        verify(accountRepository).save(accountCaptor.capture());

        assertThat(result.getCategory()).isNull();
        assertThat(accountCaptor.getValue().getCategory()).isNull();
        assertThat(accountCaptor.getValue().getOverdraftLimit()).isEqualByComparingTo(BigDecimal.ZERO);
        assertThat(accountCaptor.getValue().getCurrency()).isEqualTo(BankAccount.DEFAULT_CURRENCY);
        verify(categoryRepository, never()).findById(org.mockito.ArgumentMatchers.anyLong());
    }

    @Test
    @DisplayName("Should throw when user is not found")
    void shouldThrowWhenUserIsNotFound() {
        CreateBankAccountCommand command = new CreateBankAccountCommand(
                99L,
                "Conta",
                null,
                2L,
                null,
                null,
                null,
                null
        );

        when(userRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> handler.handle(command))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessage("User not found");

        verify(userRepository).findById(99L);
        verifyNoInteractions(systemTypeRepository, categoryRepository, accountRepository);
    }

    @Test
    @DisplayName("Should throw when system account type is not found")
    void shouldThrowWhenSystemAccountTypeIsNotFound() {
        CreateBankAccountCommand command = new CreateBankAccountCommand(
                1L,
                "Conta",
                null,
                88L,
                null,
                null,
                null,
                null
        );

        User user = User.builder().id(1L).name("Luiz").email("luiz@test.com").password("secret").build();

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(systemTypeRepository.findById(88L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> handler.handle(command))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessage("System account type not found");

        verify(userRepository).findById(1L);
        verify(systemTypeRepository).findById(88L);
        verifyNoInteractions(categoryRepository, accountRepository);
    }

    @Test
    @DisplayName("Should throw when category is not found")
    void shouldThrowWhenCategoryIsNotFound() {
        CreateBankAccountCommand command = new CreateBankAccountCommand(
                1L,
                "Conta",
                null,
                2L,
                77L,
                null,
                null,
                null
        );

        User user = User.builder().id(1L).name("Luiz").email("luiz@test.com").password("secret").build();
        SystemAccountType type = SystemAccountType.builder().id(2L).name("Checking").allowsOverdraft(true).build();

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(systemTypeRepository.findById(2L)).thenReturn(Optional.of(type));
        when(categoryRepository.findById(77L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> handler.handle(command))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessage("Category not found");

        verify(userRepository).findById(1L);
        verify(systemTypeRepository).findById(2L);
        verify(categoryRepository).findById(77L);
        verify(accountRepository, never()).save(org.mockito.ArgumentMatchers.any(BankAccount.class));
    }

    @Test
    @DisplayName("Should reject overdraft when type does not allow it")
    void shouldRejectOverdraftWhenTypeDoesNotAllowIt() {
        CreateBankAccountCommand command = new CreateBankAccountCommand(
                1L,
                "Conta Poupanca",
                null,
                2L,
                null,
                null,
                BigDecimal.ONE,
                "BRL"
        );

        User user = User.builder().id(1L).name("Luiz").email("luiz@test.com").password("secret").build();
        SystemAccountType type = SystemAccountType.builder().id(2L).name("Savings").allowsOverdraft(false).build();

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(systemTypeRepository.findById(2L)).thenReturn(Optional.of(type));

        assertThatThrownBy(() -> handler.handle(command))
                .isInstanceOf(BusinessException.class)
                .hasMessage("This account type does not allow overdraft");

        verify(userRepository).findById(1L);
        verify(systemTypeRepository).findById(2L);
        verify(categoryRepository, never()).findById(org.mockito.ArgumentMatchers.anyLong());
        verify(accountRepository, never()).save(org.mockito.ArgumentMatchers.any(BankAccount.class));
    }
}
