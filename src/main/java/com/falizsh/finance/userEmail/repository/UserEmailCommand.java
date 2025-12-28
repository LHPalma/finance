package com.falizsh.finance.userEmail.repository;

import com.falizsh.finance.user.model.User;
import com.falizsh.finance.user.repository.UserRepository;
import com.falizsh.finance.userEmail.assembler.UserEmailAssembler;
import com.falizsh.finance.userEmail.dto.UserEmailCreateDTO;
import com.falizsh.finance.userEmail.dto.UserEmailResponseDTO;
import com.falizsh.finance.userEmail.model.UserEmail;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.EntityModel;
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
