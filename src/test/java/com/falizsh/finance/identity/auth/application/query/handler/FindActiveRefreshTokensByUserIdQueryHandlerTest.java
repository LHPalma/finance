package com.falizsh.finance.identity.auth.application.query.handler;

import com.falizsh.finance.identity.auth.application.query.FindActiveRefreshTokensByUserIdQuery;
import com.falizsh.finance.identity.auth.domain.model.RefreshToken;
import com.falizsh.finance.identity.auth.domain.repository.RefreshTokenRepository;
import com.falizsh.finance.support.TestSupport;
import org.junit.jupiter.api.Test;
import org.mockito.InOrder;
import org.mockito.Mock;

import java.time.Instant;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class FindActiveRefreshTokensByUserIdQueryHandlerTest extends TestSupport {

    @Mock
    private RefreshTokenRepository refreshTokenRepository;

    private FindActiveRefreshTokensByUserIdQueryHandler handler;

    @Override
    public void init() {
        handler = new FindActiveRefreshTokensByUserIdQueryHandler(refreshTokenRepository);
    }

    @Test
    void shouldDelegateToCustomRepository() {
        Long userId = 10L;
        Instant now = Instant.parse("2026-04-11T10:00:00Z");
        List<RefreshToken> expected = List.of(mock(RefreshToken.class), mock(RefreshToken.class));
        FindActiveRefreshTokensByUserIdQuery query = new FindActiveRefreshTokensByUserIdQuery(userId, now);

        when(refreshTokenRepository.findActiveByUserId(userId, now)).thenReturn(expected);

        List<RefreshToken> result = handler.handle(query);

        assertThat(result).isEqualTo(expected);
        InOrder inOrder = inOrder(refreshTokenRepository);
        inOrder.verify(refreshTokenRepository).findActiveByUserId(userId, now);
        inOrder.verifyNoMoreInteractions();
    }
}
