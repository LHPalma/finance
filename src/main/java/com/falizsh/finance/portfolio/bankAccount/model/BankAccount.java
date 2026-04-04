package com.falizsh.finance.portfolio.bankAccount.model;

import com.falizsh.finance.identity.users.user.model.User;
import com.falizsh.finance.portfolio.bankAccount.systemAccountType.model.SystemAccountType;
import com.falizsh.finance.portfolio.bankAccount.userAccountCategory.model.UserAccountCategory;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
@Builder(access = AccessLevel.PRIVATE)
@Entity(name = "BankAccount")
@Table(name = "bank_account")
public class BankAccount {

    public static final BigDecimal DEFAULT_BALANCE = BigDecimal.valueOf(0.00);
    public static final String DEFAULT_CURRENCY = "BRL";
    public static final Boolean DEFAULT_IS_ACTIVE = true;
    public static final BigDecimal DEFAULT_OVERDRAFT_LIMIT = BigDecimal.ZERO;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @NotBlank
    @Size(max = 100)
    @Column(nullable = false, length = 100)
    private String name;

    @Size(max = 255)
    @Column(name = "description", nullable = true, length = 255)
    private String description;

    @NotNull
    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "system_type_id", nullable = false)
    private SystemAccountType type;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_category_id", nullable = true)
    private UserAccountCategory category;

    @NotNull
    @Builder.Default
    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal balance = DEFAULT_BALANCE;

    @NotNull
    @Builder.Default
    @Column(name = "overdraft_limit", nullable = false)
    private BigDecimal overdraftLimit = DEFAULT_OVERDRAFT_LIMIT;

    @NotBlank
    @Size(max = 3)
    @Column(nullable = false, length = 3)
    @Builder.Default
    private String currency = DEFAULT_CURRENCY;

    @NotNull
    @Column(nullable = false)
    @Builder.Default
    private Boolean isActive = DEFAULT_IS_ACTIVE;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(nullable = false)
    private LocalDateTime updatedAt;

    public static BankAccount create(
            User user,
            String name,
            String description,
            SystemAccountType type,
            UserAccountCategory category,
            BigDecimal overdraftLimit,
            String currency
    ) {

        if (user == null || type == null || name == null || name.isBlank()) {
            throw new IllegalArgumentException("User, Type and Name are mandatory to create a Bank Account.");
        }

        return BankAccount.builder()
                .user(user)
                .name(name)
                .description(description)
                .type(type)
                .category(category)
                .overdraftLimit(overdraftLimit != null ? overdraftLimit : DEFAULT_OVERDRAFT_LIMIT)
                .currency(currency != null && !currency.isBlank() ? currency : DEFAULT_CURRENCY)
                .balance(DEFAULT_BALANCE)
                .isActive(DEFAULT_IS_ACTIVE)
                .build();

    }

}
