package com.falizsh.finance.identity.auth.domain.repository;

import com.falizsh.finance.identity.auth.domain.model.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long>, RefreshTokenCustomRepository {
}
