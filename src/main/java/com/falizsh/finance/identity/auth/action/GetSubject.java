package com.falizsh.finance.identity.auth.action;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.falizsh.finance.identity.auth.usecase.GenerateJWTTokenUseCase;
import org.springframework.stereotype.Component;

@Component
public class GetSubject implements GetSubjectAction {

    private final GenerateJWTTokenUseCase tokenUseCase;

    public GetSubject(GenerateJWTTokenUseCase tokenUseCase) {
        this.tokenUseCase = tokenUseCase;
    }

    @Override
    public String from(String tokenJWT) {
        DecodedJWT decodedJWT = tokenUseCase.verifyAccessToken(tokenJWT);
        return decodedJWT == null ? null : decodedJWT.getSubject();
    }
}
