package com.falizsh.finance.identity.auth.infrastructure.persistence;

import com.falizsh.finance.identity.auth.domain.model.RefreshToken;
import com.falizsh.finance.support.TestSupport;
import com.querydsl.core.types.Predicate;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.junit.jupiter.api.Test;
import org.mockito.InOrder;
import org.mockito.Mock;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class RefreshTokenCustomRepositoryImplTest extends TestSupport {

    @Mock
    private JPAQueryFactory queryFactory;

    @Mock
    private JPAQuery<RefreshToken> jpaQuery;

    private RefreshTokenCustomRepositoryImpl repository;

    @Override
    public void init() {
        repository = new RefreshTokenCustomRepositoryImpl(queryFactory);
    }

    @Test
    void shouldFindTokenByTokenId() {
        UUID tokenId = UUID.randomUUID();
        RefreshToken refreshToken = org.mockito.Mockito.mock(RefreshToken.class);

        when(queryFactory.selectFrom(com.falizsh.finance.identity.auth.domain.model.QRefreshToken.refreshToken))
                .thenReturn(jpaQuery);
        when(jpaQuery.where(any(Predicate.class))).thenReturn(jpaQuery);
        when(jpaQuery.fetchOne()).thenReturn(refreshToken);

        var result = repository.findByTokenId(tokenId);

        assertThat(result).isPresent();
        assertThat(result.get()).isEqualTo(refreshToken);

        InOrder inOrder = inOrder(queryFactory);
        inOrder.verify(queryFactory).selectFrom(com.falizsh.finance.identity.auth.domain.model.QRefreshToken.refreshToken);
    }

    @Test
    void shouldFindOnlyActiveTokensByUserId() {
        Long userId = 17L;
        Instant now = Instant.parse("2026-04-11T12:00:00Z");
        List<RefreshToken> expected = List.of(
                org.mockito.Mockito.mock(RefreshToken.class),
                org.mockito.Mockito.mock(RefreshToken.class)
        );

        when(queryFactory.selectFrom(com.falizsh.finance.identity.auth.domain.model.QRefreshToken.refreshToken))
                .thenReturn(jpaQuery);
        when(jpaQuery.where(any(Predicate.class), any(Predicate.class), any(Predicate.class))).thenReturn(jpaQuery);
        when(jpaQuery.fetch()).thenReturn(expected);

        List<RefreshToken> result = repository.findActiveByUserId(userId, now);

        assertThat(result).isEqualTo(expected);
        InOrder inOrder = inOrder(queryFactory);
        inOrder.verify(queryFactory).selectFrom(com.falizsh.finance.identity.auth.domain.model.QRefreshToken.refreshToken);
    }
}
