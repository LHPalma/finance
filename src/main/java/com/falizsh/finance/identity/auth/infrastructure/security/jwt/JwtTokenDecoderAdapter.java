package com.falizsh.finance.identity.auth.infrastructure.security.jwt;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.falizsh.finance.identity.auth.application.port.TokenDecoder;
import com.falizsh.finance.identity.auth.application.port.TokenGenerator;
import org.springframework.stereotype.Component;

@Component
public class JwtTokenDecoderAdapter implements TokenDecoder {

    private final TokenGenerator tokenUseCase;

    public JwtTokenDecoderAdapter(TokenGenerator tokenUseCase) {
        this.tokenUseCase = tokenUseCase;
    }

    @Override
    public String from(String tokenJWT) {
        DecodedJWT decodedJWT = tokenUseCase.verifyAccessToken(tokenJWT);
        return decodedJWT == null ? null : decodedJWT.getSubject();
    }
}
