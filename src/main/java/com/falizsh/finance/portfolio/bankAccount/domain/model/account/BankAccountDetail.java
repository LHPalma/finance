package com.falizsh.finance.portfolio.bankAccount.domain.model.account;

import com.falizsh.finance.identity.users.user.model.User;
import com.falizsh.finance.infrastructure.exception.BusinessException;
import com.falizsh.finance.portfolio.bankAccount.domain.model.type.SystemAccountType;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;

@Getter
@SuperBuilder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Entity
@Table(name = "bank_account_detail")
@PrimaryKeyJoinColumn(name = "account_id")
public class BankAccountDetail extends Account {

    public static final BigDecimal DEFAULT_BALANCE = BigDecimal.ZERO;
    public static final BigDecimal DEFAULT_OVERDRAFT_LIMIT = BigDecimal.ZERO;
    public static final String DEFAULT_CURRENCY = "BRL";

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "system_type_id", nullable = false)
    private SystemAccountType systemType;

    @Builder.Default
    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal balance = DEFAULT_BALANCE;

    @Builder.Default
    @Column(name = "overdraft_limit", nullable = false, precision = 15, scale = 2)
    private BigDecimal overdraftLimit = DEFAULT_OVERDRAFT_LIMIT;

    @Builder.Default
    @Column(nullable = false, length = 3)
    private String currency = DEFAULT_CURRENCY;

    @PrePersist
    protected void onBankCreate() {
        this.defineNature(AccountNature.ASSET);
    }

    public static BankAccountDetail create(
            User user,
            String name,
            String description,
            SystemAccountType systemType,
            BigDecimal initialBalance,
            BigDecimal overdraftLimit,
            String currency
    ) {
        validateOverdraft(systemType, overdraftLimit);

        BigDecimal resolvedBalance = (initialBalance != null && initialBalance.compareTo(BigDecimal.ZERO) > 0)
                ? initialBalance
                : DEFAULT_BALANCE;

        return BankAccountDetail.builder()
                .user(user)
                .name(name)
                .description(description)
                .systemType(systemType)
                .balance(resolvedBalance)
                .overdraftLimit(overdraftLimit != null ? overdraftLimit : DEFAULT_OVERDRAFT_LIMIT)
                .currency(currency != null ? currency : DEFAULT_CURRENCY)
                .build();
    }

    public void updateOverdraftLimit(BigDecimal newLimit) {
        validateOverdraft(this.systemType, newLimit);
        this.overdraftLimit = newLimit;
    }

    /**
     * Aplica um débito no cache de saldo (Ativo ↑).
     * Chamado quando dinheiro ENTRA nessa conta.
     */
    public void applyDebit(BigDecimal amount) {
        this.balance = this.balance.add(amount);
    }

    /**
     * Aplica um crédito no cache de saldo (Ativo ↓).
     * Chamado quando dinheiro SAI dessa conta.
     * Valida que o resultado não ultrapasse o limite do cheque especial.
     */
    public void applyCredit(BigDecimal amount) {
        BigDecimal availableFunds = this.balance.add(this.overdraftLimit);
        if (amount.compareTo(availableFunds) > 0) {
            throw new BusinessException(
                "Saldo insuficiente. Disponível: %s, Solicitado: %s."
                    .formatted(availableFunds.toPlainString(), amount.toPlainString())
            );
        }
        this.balance = this.balance.subtract(amount);
    }

    private static void validateOverdraft(SystemAccountType systemType, BigDecimal targetLimit) {
        validateOverdraftLimitAmount(targetLimit);
        validateOverdraftPolicy(systemType, targetLimit);
    }

    private static void validateOverdraftLimitAmount(BigDecimal targetLimit) {
        if (targetLimit != null && targetLimit.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("O limite do cheque especial não pode ser negativo.");
        }
    }

    private static void validateOverdraftPolicy(SystemAccountType systemType, BigDecimal targetLimit) {
        if (!systemType.isAllowsOverdraft()
                && targetLimit != null
                && targetLimit.compareTo(BigDecimal.ZERO) > 0) {
            throw new BusinessException("This account type does not allow overdraft");
        }
    }
}