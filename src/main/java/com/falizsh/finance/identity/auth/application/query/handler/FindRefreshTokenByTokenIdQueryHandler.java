package com.falizsh.finance.identity.auth.application.query.handler;

import com.falizsh.finance.identity.auth.application.query.FindRefreshTokenByTokenIdQuery;
import com.falizsh.finance.identity.auth.domain.model.RefreshToken;
import com.falizsh.finance.identity.auth.domain.repository.RefreshTokenCustomRepository;
import com.falizsh.finance.identity.auth.domain.repository.RefreshTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class FindRefreshTokenByTokenIdQueryHandler {

    private final RefreshTokenRepository refreshTokenRepository;

    public Optional<RefreshToken> handle(FindRefreshTokenByTokenIdQuery query) {
        return refreshTokenRepository.findByTokenId(query.tokenId());
    }

}
