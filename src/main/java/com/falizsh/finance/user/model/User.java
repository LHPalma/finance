package com.falizsh.finance.user.model;

import com.falizsh.finance.userEmail.model.UserEmail;
import com.falizsh.finance.userEmail.model.UserEmailType;
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
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Entity(name = "User")
@Table(name = "user")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String name;

    @NotBlank
    @Email
    private String email;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @NotBlank
    private String password;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @NotBlank
    private String salt;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    @Enumerated(EnumType.STRING)
    @NotNull
    private Status status = Status.ACTIVE;

    // #region UserEmail
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<UserEmail> emails = new ArrayList<>();

    public void addEmail(String email, UserEmailType type, boolean isPrimary) {
        boolean alreadyExists = this.emails.stream()
                .anyMatch(e -> e.getEmail().equalsIgnoreCase(email));
        if (alreadyExists) {
            throw new IllegalArgumentException("Este email já está vinculado a este usuário");
        }

        if (isPrimary) {
            this.emails.forEach(UserEmail::removePrimary);
        } else {

            if (this.emails.isEmpty()) {
                isPrimary = true;
            }

        }

        UserEmail newEmail = new UserEmail(this, email, type, isPrimary);
        this.emails.add(newEmail);

    }

    public Collection<UserEmail> getEmails() {
        return Collections.unmodifiableCollection(emails);
    }
    // #endregion UserEmail

}
