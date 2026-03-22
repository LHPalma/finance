package com.falizsh.finance.infrastructure.security.encoder;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.argon2.Argon2PasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class Argon2WithPepperEncoder implements PasswordEncoder {

    private final Argon2PasswordEncoder argon2Encoder;
    private final String pepper;

    public Argon2WithPepperEncoder(
            @Value("${security.password.pepper}") String pepper,
            @Value("${security.password.argon2.salt-length}") int saltLength,
            @Value("${security.password.argon2.hash-length}") int hashLength,
            @Value("${security.password.argon2.parallelism}") int parallelism,
            @Value("${security.password.argon2.memory}") int memory,
            @Value("${security.password.argon2.iterations}") int iterations
    ) {
        this.pepper = pepper;
        this.argon2Encoder = new Argon2PasswordEncoder(
                saltLength,
                hashLength,
                parallelism,
                memory,
                iterations
        );

    }

    @Override
    public String encode(CharSequence rawPassword) {
        if (rawPassword == null) {
            throw new IllegalArgumentException("Password cannot be null");
        }

        String passwordWithPepper = rawPassword + pepper;

        return argon2Encoder.encode(passwordWithPepper);
    }

    @Override
    public boolean matches(CharSequence rawPassword, String encodedPassword) {
        if (rawPassword == null || encodedPassword == null) {
            return false;
        }

        String passwordWithPepper = rawPassword + pepper;

        return argon2Encoder.matches(passwordWithPepper, encodedPassword);
    }

    @Override
    public boolean upgradeEncoding(String encodedPassword) {
        return argon2Encoder.upgradeEncoding(encodedPassword);
    }
}