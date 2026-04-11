package com.falizsh.finance.identity.auth.application.query.handler;

import com.falizsh.finance.identity.auth.application.query.FindRefreshTokenByTokenIdQuery;
import com.falizsh.finance.identity.auth.domain.model.RefreshToken;
import com.falizsh.finance.identity.auth.domain.repository.RefreshTokenRepository;
import com.falizsh.finance.support.TestSupport;
import org.junit.jupiter.api.Test;
import org.mockito.InOrder;
import org.mockito.Mock;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class FindRefreshTokenByTokenIdQueryHandlerTest extends TestSupport {

    @Mock
    private RefreshTokenRepository refreshTokenRepository;

    private FindRefreshTokenByTokenIdQueryHandler handler;

    @Override
    public void init() {
        handler = new FindRefreshTokenByTokenIdQueryHandler(refreshTokenRepository);
    }

    @Test
    void shouldDelegateToCustomRepository() {
        UUID tokenId = UUID.randomUUID();
        Optional<RefreshToken> expected = Optional.of(mock(RefreshToken.class));
        FindRefreshTokenByTokenIdQuery query = new FindRefreshTokenByTokenIdQuery(tokenId);

        when(refreshTokenRepository.findByTokenId(tokenId)).thenReturn(expected);

        Optional<RefreshToken> result = handler.handle(query);

        assertThat(result).isEqualTo(expected);
        InOrder inOrder = inOrder(refreshTokenRepository);
        inOrder.verify(refreshTokenRepository).findByTokenId(tokenId);
        inOrder.verifyNoMoreInteractions();
    }
}
