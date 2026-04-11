package com.falizsh.finance.identity.auth.application.port;

public interface TokenDecoder {
    String from(String tokenJWT);
}
