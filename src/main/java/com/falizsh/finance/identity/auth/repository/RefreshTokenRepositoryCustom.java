package com.falizsh.finance.identity.auth.repository;

import com.falizsh.finance.identity.auth.model.RefreshToken;

import java.util.Optional;

public interface RefreshTokenRepositoryCustom {

    Optional<RefreshToken> findByToken(String token);
}
