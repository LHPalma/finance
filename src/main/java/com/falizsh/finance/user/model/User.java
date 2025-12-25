package com.falizsh.finance.user.model;

import com.falizsh.finance.shared.valueObject.CEP;
import com.falizsh.finance.user.dto.UserCreateDTO;
import com.falizsh.finance.userAddress.model.UserAddress;
import com.falizsh.finance.userAddress.model.UserAddressType;
import com.falizsh.finance.userEmail.model.UserEmail;
import com.falizsh.finance.userEmail.model.UserEmailType;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
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
@Builder(builderClassName = "UserBuilder", toBuilder = true)
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
    private UserStatus status = UserStatus.ACTIVE;


    public static User to(UserCreateDTO data, String password, String salt) {
        User user = User.builder()
                .name(data.name())
                .email(data.email())
                .password(password)
                .salt(salt)
                .status(UserStatus.ACTIVE)
                .emails(new ArrayList<>())
                .addresses(new ArrayList<>())
                .build();
        return user;
    }


    //region UserEmail aggregate
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<UserEmail> emails = new ArrayList<>();

    public Collection<UserEmail> getEmails() {
        return Collections.unmodifiableCollection(emails);
    }

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

    //endregion UserEmail aggregate


    //region UserAddress aggregate
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<UserAddress> addresses = new ArrayList<>();

    public Collection<UserAddress> getAddresses() {
        return Collections.unmodifiableCollection(addresses);
    }

    public void addAddress(
            UserAddressType type,
            String street,
            String number,
            String complement,
            String neighborhood,
            String city,
            String state,
            CEP zipCode,
            String country,
            Boolean isPrimary
    ) {
        if (Boolean.TRUE.equals(isPrimary)) {
            this.addresses.forEach(UserAddress::removePrimary);
        } else {

            if (this.addresses.isEmpty()) {
                isPrimary = true;
            }

        }

        UserAddress newAddress = UserAddress.builder()
                .user(this)
                .type(type)
                .street(street)
                .number(number)
                .complement(complement)
                .neighborhood(neighborhood)
                .city(city)
                .state(state)
                .zipCode(zipCode)
                .country( country == null || country.isBlank() ? UserAddress.DEFAULT_COUNTRY : country )
                .isPrimary(isPrimary)
                .build();


        this.addresses.add(newAddress);
    }

    //endregion UserAddress aggregate

}
