package com.falizsh.finance.userEmail.service;

import com.falizsh.finance.user.model.User;
import com.falizsh.finance.user.repository.UserRepository;
import com.falizsh.finance.userEmail.assembler.UserEmailAssembler;
import com.falizsh.finance.userEmail.dto.UserEmailCreateDTO;
import com.falizsh.finance.userEmail.dto.UserEmailResponseDTO;
import com.falizsh.finance.userEmail.model.UserEmail;
import com.falizsh.finance.userEmail.repository.UserEmailRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.EntityModel;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
public class UserEmailService {

    private final UserRepository userRepository;
    private final UserEmailAssembler assembler;

    @Deprecated // TODO: Apagar ao final da refatoração
    private final UserEmailRepository emailRepository;

    public Page<UserEmail> findAllByUserId(Long userId, Pageable pageable) {
        return emailRepository.findAllByUserId(userId, pageable);
    }


    @Transactional
    public EntityModel<UserEmailResponseDTO> create(Long userId, UserEmailCreateDTO dto) {

        User user = userRepository.findById(userId).
                orElseThrow(() -> new IllegalArgumentException("Usuário não encontrado"));

        user.addEmail(dto.getEmail(), dto.getType(), dto.getIsPrimary());

        userRepository.save(user);

        UserEmail savedEmail = user.getEmails().stream()
                .filter(e -> e.getEmail().equals(dto.getEmail()))
                .findFirst()
                .orElseThrow();

        return assembler.toModel(savedEmail);
    }

}
