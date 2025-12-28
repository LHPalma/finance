package com.falizsh.finance.users.userEmail.repository;

import com.falizsh.finance.users.user.model.User;
import com.falizsh.finance.users.user.repository.UserRepository;
import com.falizsh.finance.users.userEmail.dto.UserEmailCreateDTO;
import com.falizsh.finance.users.userEmail.model.UserEmail;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class UserEmailCommand {

    private final UserRepository userRepository;

    @Transactional
    public UserEmail create(Long userId, UserEmailCreateDTO dto) {

        User user = userRepository.findById(userId).
                orElseThrow(() -> new IllegalArgumentException("Usuário não encontrado"));

        user.addEmail(dto.getEmail(), dto.getType(), dto.getIsPrimary());

        userRepository.save(user);

        UserEmail savedEmail = user.getEmails().stream()
                .filter(e -> e.getEmail().equals(dto.getEmail()))
                .findFirst()
                .orElseThrow();

        return savedEmail;
    }
}
