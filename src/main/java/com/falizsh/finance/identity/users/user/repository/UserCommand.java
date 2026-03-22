package com.falizsh.finance.identity.users.user.repository;

import com.falizsh.finance.infrastructure.exception.DuplicatedDataException;
import com.falizsh.finance.identity.users.user.dto.UserCreateDTO;
import com.falizsh.finance.identity.users.user.model.User;
import com.falizsh.finance.identity.users.user.model.UserStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(rollbackFor = Exception.class)
public class UserCommand {

    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;

    public User create(UserCreateDTO data) {
        if (repository.existsByEmail(data.email())) {
            throw new DuplicatedDataException("business.user.email.already.exists");
        }

        String hashedPassword = passwordEncoder.encode(data.password());

        log.info("Creating new user with email: {}", data.email());

        User user = User.builder()
                .name(data.name())
                .email(data.email())
                .password(hashedPassword)
                .status(UserStatus.ACTIVE)
                .emails(new ArrayList<>())
                .addresses(new ArrayList<>())
                .telephones(new ArrayList<>())
                .build();

        return repository.save(user);
    }
}