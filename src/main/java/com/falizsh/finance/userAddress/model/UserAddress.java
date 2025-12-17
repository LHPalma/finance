package com.falizsh.finance.userAddress.model;

import com.falizsh.finance.shared.valueObject.CEP;
import com.falizsh.finance.user.model.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.Valid;
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
@Entity(name = "UserAddress")
@Table(name = "user_address")
public class UserAddress {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserAddressType type = UserAddressType.RESIDENTIAL;

    @NotBlank
    @Column(length = 70, nullable = false)
    private String street;

    @NotBlank
    @Column(length = 10, nullable = false)
    private String number;

    @Column(length = 100)
    private String complement;

    @Column(length = 100)
    private String neighborhood;

    @Column(length = 100, nullable = false)
    private String city;

    @Column(length = 2, nullable = false)
    private String state;

    @Valid
    @Embedded
    private CEP zipCode;

    private String country = "BRA";

    private Boolean isPrimary = false;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    @Enumerated(EnumType.STRING)
    private UserAddressStatus status = UserAddressStatus.ACTIVE;


    public UserAddress(
            User user,
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

        this.user = Objects.requireNonNull(user, "user must not be null");
        this.type = Objects.requireNonNull(type, "type must not be null");

        Objects.requireNonNull(street, "street must not be null");
        if (street.isBlank()) throw new IllegalArgumentException("street must not be blank");
        this.street = street;

        Objects.requireNonNull(number, "number must not be null");
        if (number.isBlank()) throw new IllegalArgumentException("number must not be blank");
        this.number = number;

        this.complement = complement;
        this.neighborhood = neighborhood;

        Objects.requireNonNull(city, "city must not be null");
        if (city.isBlank()) throw new IllegalArgumentException("city must not be blank");
        this.city = city;

        Objects.requireNonNull(state, "state must not be null");
        if (state.isBlank()) throw new IllegalArgumentException("state must not be blank");
        this.state = state;

        this.zipCode = Objects.requireNonNull(zipCode, "zipCode must not be null");

        this.country = (country == null) ? "BRA" : country;
        this.isPrimary = Boolean.TRUE.equals(isPrimary);

    }

    public void removePrimary() {
        this.isPrimary = false;
    }


}
