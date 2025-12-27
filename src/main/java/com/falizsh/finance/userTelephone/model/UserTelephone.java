package com.falizsh.finance.userTelephone.model;

import com.falizsh.finance.user.model.User;
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

import java.time.LocalDateTime;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "UserTelephone")
@Table(name = "user_telephone")
public class UserTelephone {

    public final static TelephoneType DEFAULT_TYPE = TelephoneType.PERSONAL;
    public final static UserTelephoneStatus DEFAULT_STATUS = UserTelephoneStatus.ACTIVE;
    public final static Boolean DEFAULT_IS_PRIMARY = false;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Builder.Default
    private TelephoneType type = DEFAULT_TYPE;

    @NotBlank
    @Size(max = 5)
    @Column(nullable = false, length = 5)
    private String areaCode;

    @NotBlank
    @Size(max = 10)
    @Column(nullable = false, length = 10)
    private String telephone;

    @NotNull
    @Column(nullable = false)
    @Builder.Default
    Boolean isPrimary = DEFAULT_IS_PRIMARY;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(nullable = false)
    private LocalDateTime updatedAt;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Builder.Default
    private UserTelephoneStatus status = DEFAULT_STATUS;

}
