package com.falizsh.finance.identity.auth.repository;

import com.falizsh.finance.identity.auth.model.QRefreshToken;
import com.falizsh.finance.identity.auth.model.RefreshToken;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
@Repository
@Transactional(readOnly = true)
public class RefreshTokenRepositoryCustomImpl implements RefreshTokenRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public Optional<RefreshToken> findByToken(String token) {
        QRefreshToken qRefreshToken = QRefreshToken.refreshToken;

        RefreshToken result = queryFactory
                .selectFrom(qRefreshToken)
                .where(qRefreshToken.tokenId.eq(UUID.fromString(token)))
                .fetchOne();

        return Optional.ofNullable(result);
    }

}
