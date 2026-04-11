package com.falizsh.finance.identity.auth.domain.repository;

import com.falizsh.finance.identity.auth.domain.model.QRefreshToken;
import com.falizsh.finance.identity.auth.domain.model.RefreshToken;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RefreshTokenRepositoryImpl implements RefreshTokenCustomRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public Optional<RefreshToken> findByTokenId(UUID tokenId) {
        QRefreshToken refreshToken = QRefreshToken.refreshToken;

        RefreshToken result = queryFactory
                .selectFrom(refreshToken)
                .where(refreshToken.tokenId.eq(tokenId))
                .fetchOne();

        return Optional.ofNullable(result);
    }

    @Override
    public List<RefreshToken> findActiveByUserId(Long userId, Instant now) {
        QRefreshToken refreshToken = QRefreshToken.refreshToken;

        return queryFactory
                .selectFrom(refreshToken)
                .where(
                        refreshToken.user.id.eq(userId),
                        refreshToken.revokedAt.isNull(),
                        refreshToken.expiresAt.after(now)
                )
                .fetch();
    }
}
