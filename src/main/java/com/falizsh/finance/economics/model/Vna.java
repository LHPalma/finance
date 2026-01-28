package com.falizsh.finance.economics.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;

@Getter
@Builder(toBuilder = true, builderClassName = "builder")
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "Vna")
@Table(
        name = "vna",
        indexes = {
                @Index(name = "idx_vna_selic", columnList = "selic_code"),
                @Index(name = "idx_vna_date", columnList = "reference_date")
        },
        uniqueConstraints = {
                @UniqueConstraint(name = "uq_vna_selic_date",
                        columnNames = {
                                "selic_code",
                                "reference_date"
                        }
                )
        })
public class Vna {

    public static final VnaStatus DEFAULT_STATUS = VnaStatus.F;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Size(max = 50)
    @NotNull
    @Column(name = "security", nullable = false, length = 50)
    private String security;

    @Size(max = 20)
    @NotNull
    @Column(name = "selic_code", nullable = false, length = 20)
    private String selicCode;

    @NotNull
    @Column(name = "reference_date", nullable = false)
    private LocalDate referenceDate;

    @NotNull
    @Column(name = "price", nullable = false, precision = 25, scale = 8)
    private BigDecimal price;

    @Column(name = "index_value", precision = 25, scale = 8)
    private BigDecimal indexValue;

    @Enumerated(EnumType.STRING)
    @Builder.Default
    private VnaStatus status = DEFAULT_STATUS;

    @CreationTimestamp
    @ColumnDefault("CURRENT_TIMESTAMP")
    @Column(name = "created_at")
    private Instant createdAt;


}

