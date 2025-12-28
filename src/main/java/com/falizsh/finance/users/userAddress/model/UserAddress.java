package com.falizsh.finance.users.userAddress.model;

import com.falizsh.finance.shared.valueObject.CEP;
import com.falizsh.finance.users.user.model.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder(builderClassName = "UserAddressBuilder", toBuilder = true)
@Entity(name = "UserAddress")
@Table(name = "user_address")
public class UserAddress {

    public static final String DEFAULT_COUNTRY = "BRA";

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
    @Builder.Default
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

    @Builder.Default
    private String country = DEFAULT_COUNTRY;

    @Builder.Default
    private Boolean isPrimary = false;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    @Enumerated(EnumType.STRING)
    @Builder.Default
    private UserAddressStatus status = UserAddressStatus.ACTIVE;


    public void removePrimary() {
        this.isPrimary = false;
    }

}
