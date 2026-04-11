package com.falizsh.finance.identity.auth.application.port;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.falizsh.finance.identity.users.user.model.User;

import java.util.UUID;

public interface TokenGenerator {

    String generate(User user);

    String generateRefreshToken(User user, UUID tokenId);

    DecodedJWT verifyAccessToken(String token);

    DecodedJWT verifyRefreshToken(String token);

}
