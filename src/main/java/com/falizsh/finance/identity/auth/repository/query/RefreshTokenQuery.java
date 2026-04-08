package com.falizsh.finance.identity.auth.repository.query;

import com.falizsh.finance.identity.auth.model.RefreshToken;

import java.util.Optional;

public interface RefreshTokenQuery {

    Optional<RefreshToken> findByToken(String token);

}
