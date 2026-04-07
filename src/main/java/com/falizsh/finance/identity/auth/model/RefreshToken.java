package com.falizsh.finance.identity.auth.model;

import com.falizsh.finance.identity.users.user.model.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.util.UUID;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "refresh_token")
public class RefreshToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "token_id", nullable = false, unique = true)
    private UUID tokenId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "expires_at", nullable = false)
    private Instant expiresAt;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @Column(name = "revoked_at")
    private Instant revokedAt;

    @Column(name = "replaced_by_token_id")
    private UUID replacedByTokenId;

    private RefreshToken(UUID tokenId, User user, Instant expiresAt) {
        this.tokenId = tokenId;
        this.user = user;
        this.expiresAt = expiresAt;
    }

    public static RefreshToken create(User user, Duration ttl, Clock clock) {
        Instant now = clock.instant();
        return new RefreshToken(UUID.randomUUID(), user, now.plus(ttl));
    }

    public boolean isActive(Instant now) {
        return revokedAt == null && expiresAt.isAfter(now);
    }

    public boolean isRevoked() {
        return revokedAt != null;
    }

    public void revoke(Instant revokedAt, UUID replacedByTokenId) {
        this.revokedAt = revokedAt;
        this.replacedByTokenId = replacedByTokenId;
    }
}
