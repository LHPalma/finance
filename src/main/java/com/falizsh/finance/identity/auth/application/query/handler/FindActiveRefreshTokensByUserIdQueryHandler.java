package com.falizsh.finance.identity.auth.application.query.handler;

import com.falizsh.finance.identity.auth.application.query.FindActiveRefreshTokensByUserIdQuery;
import com.falizsh.finance.identity.auth.domain.model.RefreshToken;
import com.falizsh.finance.identity.auth.domain.repository.RefreshTokenCustomRepository;
import com.falizsh.finance.identity.auth.domain.repository.RefreshTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class FindActiveRefreshTokensByUserIdQueryHandler {

    private final RefreshTokenRepository refreshTokenRepository;

    public List<RefreshToken> handle(FindActiveRefreshTokensByUserIdQuery query) {
        return refreshTokenRepository.findActiveByUserId(query.userId(), query.now());
    }

}
