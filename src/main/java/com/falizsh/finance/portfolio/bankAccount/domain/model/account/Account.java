package com.falizsh.finance.portfolio.bankAccount.domain.model.account;

import com.falizsh.finance.identity.users.user.model.User;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Getter
@SuperBuilder
@Inheritance(strategy = InheritanceType.JOINED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity(name = "Account")
@Table(name = "account")
public class Account {

    public static final boolean DEFAULT_IS_ACTIVE = true;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(length = 255)
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private AccountNature nature;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_account_id")
    private Account parentAccount;

    @Column(name = "created_at", nullable = false, updatable = false)
    @CreationTimestamp
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    @UpdateTimestamp
    private LocalDateTime updatedAt;

    @Column(name = "is_active", nullable = false)
    @Builder.Default
    private Boolean isActive = DEFAULT_IS_ACTIVE;

    protected void defineNature(AccountNature nature) {
        this.nature = nature;
    }

    /**
     * Cria uma conta de Equity para registrar saldos iniciais (contrapartida de abertura).
     * Cada usuário deve ter no máximo uma conta deste tipo — use {@code AccountRepository}
     * para verificar antes de chamar este método.
     */
    public static Account createOpeningBalanceEquity(User user) {
        return Account.builder()
                .user(user)
                .name("Capital Inicial")
                .description("Conta de contrapartida para registro de saldos iniciais")
                .nature(AccountNature.EQUITY)
                .build();
    }
}
