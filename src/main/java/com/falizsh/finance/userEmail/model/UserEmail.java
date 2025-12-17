package com.falizsh.finance.userEmail.model;

import com.falizsh.finance.user.model.User;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.Objects;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Entity(name = "UserEmail")
@Table(name = "user_email")
public class UserEmail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private User user;

    @Enumerated(EnumType.STRING)
    @NotNull(message = "Tipo de email obrigatório")
    @Column(nullable = false)
    private UserEmailType type = UserEmailType.PERSONAL;

    @NotBlank(message = "Email é obrigatório")
    @Email(message = "Email inválido")
    @Column(nullable = false, length = 100)
    private String email;

    @Column(nullable = false)
    private Boolean isPrimary = false;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(nullable = false)
    private LocalDateTime updatedAt;

    @Enumerated(EnumType.STRING)
    @NotNull
    @Column(nullable = false)
    private EmailStatus status = EmailStatus.ACTIVE;

    public UserEmail(User user, String email, UserEmailType type, boolean isPrimary) {
        this.user = Objects.requireNonNull(user, "User não pode ser nulo");
        this.type = Objects.requireNonNull(type, "User não pode ser nulo");
        if (email == null || email.isBlank())
            throw new IllegalArgumentException("Email vazio");
        this.email = email;
        this.isPrimary = isPrimary;
        this.status = EmailStatus.ACTIVE;
    }

    public void removePrimary() {
        this.isPrimary = false;
    }
}
