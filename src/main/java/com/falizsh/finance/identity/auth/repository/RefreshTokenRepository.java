package com.falizsh.finance.identity.auth.repository;

import com.falizsh.finance.identity.auth.model.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {

    Optional<RefreshToken> findByTokenId(UUID tokenId);

    List<RefreshToken> findAllByUserIdAndRevokedAtIsNullAndExpiresAtAfter(Long userId, Instant now);

}
