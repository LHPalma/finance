package com.falizsh.finance.identity.auth.repository.query;

import com.falizsh.finance.identity.auth.model.RefreshToken;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RefreshTokenQueryImpl implements RefreshTokenQuery {

    @Override
    public Optional<RefreshToken> findByToken(String token) {
        return Optional.empty();
    }

}
