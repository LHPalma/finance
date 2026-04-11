package com.falizsh.finance.identity.auth.action;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.falizsh.finance.identity.auth.application.port.TokenGenerator;
import com.falizsh.finance.identity.auth.infrastructure.security.jwt.JwtTokenDecoderAdapter;
import com.falizsh.finance.support.TestSupport;
import org.junit.jupiter.api.Test;
import org.mockito.InOrder;
import org.mockito.Mock;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

class GetSubjectTest extends TestSupport {

    @Mock
    private TokenGenerator tokenGenerator;

    @Mock
    private DecodedJWT decodedJWT;

    private JwtTokenDecoderAdapter jwtTokenDecoderAdapter;

    @Override
    public void init() {
        jwtTokenDecoderAdapter = new JwtTokenDecoderAdapter(tokenGenerator);
    }

    @Test
    void shouldExtractSubjectFromValidAccessToken() {
        String token = "access.token";
        when(tokenGenerator.verifyAccessToken(token)).thenReturn(decodedJWT);
        when(decodedJWT.getSubject()).thenReturn("test@email.com");

        String subject = jwtTokenDecoderAdapter.from(token);

        assertThat(subject).isEqualTo("test@email.com");

        InOrder inOrder = inOrder(tokenGenerator, decodedJWT);
        inOrder.verify(tokenGenerator).verifyAccessToken(token);
        inOrder.verify(decodedJWT).getSubject();
        inOrder.verifyNoMoreInteractions();
    }

    @Test
    void shouldReturnNullWhenTokenIsInvalid() {
        String token = "invalid.token";
        when(tokenGenerator.verifyAccessToken(token)).thenReturn(null);

        String subject = jwtTokenDecoderAdapter.from(token);

        assertThat(subject).isNull();

        InOrder inOrder = inOrder(tokenGenerator);
        inOrder.verify(tokenGenerator).verifyAccessToken(token);
        inOrder.verifyNoMoreInteractions();
    }
}
