package com.falizsh.finance.identity.auth.usecase;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.falizsh.finance.identity.users.user.model.User;

import java.util.UUID;

public interface GenerateJWTTokenUseCase {

    String generate(User user);

    String generateRefreshToken(User user, UUID tokenId);

    DecodedJWT verifyAccessToken(String token);

    DecodedJWT verifyRefreshToken(String token);

}
