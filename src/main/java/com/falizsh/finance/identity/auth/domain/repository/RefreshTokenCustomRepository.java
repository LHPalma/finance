package com.falizsh.finance.identity.auth.domain.repository;

import com.falizsh.finance.identity.auth.domain.model.RefreshToken;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface RefreshTokenCustomRepository {

    Optional<RefreshToken> findByTokenId(UUID tokenId);

    List<RefreshToken> findActiveByUserId(Long userId, Instant now);

}
