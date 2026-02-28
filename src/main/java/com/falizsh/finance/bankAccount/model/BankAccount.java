package com.falizsh.finance.bankAccount.model;

import com.falizsh.finance.bankAccount.systemAccountType.model.SystemAccountType;
import com.falizsh.finance.bankAccount.userAccountCategory.model.UserAccountCategory;
import com.falizsh.finance.users.user.model.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
@Entity(name = "BankAccount")
@Table(name = "bank_account")
public class BankAccount {

    public static final BigDecimal DEFAULT_BALANCE = BigDecimal.valueOf(0.00);
    public static final String DEFAULT_CURRENCY = "BRL";
    public static final Boolean DEFAULT_IS_ACTIVE = true;

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
    private BigDecimal overdraftLimit = BigDecimal.ZERO;

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

}
