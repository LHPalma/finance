package com.falizsh.finance.shared.holiday.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity(name = "Holiday")
@Table(name = "Holiday", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"date", "country_code"})
})

public class Holiday {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Long id;

    @Column(nullable = false)
    private LocalDate date;

    @Column(nullable = false, length = 255)
    private String name;

    @Enumerated(EnumType.STRING)
    @Size(max = 2)
    @NotNull
    @Column(name = "country_code", nullable = false, length = 2)
    private CountryCode countryCode;

    @CreationTimestamp
    private LocalDateTime createdAt;

}
