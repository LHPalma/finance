package com.falizsh.finance.email.service;

import com.falizsh.finance.email.assembler.UserEmailAssembler;
import com.falizsh.finance.email.dto.UserEmailCreateDTO;
import com.falizsh.finance.email.dto.UserEmailResponseDTO;
import com.falizsh.finance.email.model.UserEmail;
import com.falizsh.finance.email.repository.UserEmailRepository;
import com.falizsh.finance.user.model.User;
import com.falizsh.finance.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.EntityModel;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
@RequiredArgsConstructor
public class UserEmailService {

    private final UserEmailRepository emailRepository;
    private final UserRepository userRepository;
    private final UserEmailAssembler assembler;


    public Page<UserEmail> findAll(Pageable pageable) {
        return emailRepository.findAll(pageable);
    }


    public List<UserEmail> findAllByUserId(Long userId) {
        return emailRepository.findAllByUserId(userId);
    }

    public Page<UserEmail> findAllByUserId(Long userId, Pageable pageable){
        return emailRepository.findAllByUserId(userId, pageable );
    }



    @Transactional
    public EntityModel<UserEmailResponseDTO> create(Long userId, UserEmailCreateDTO dto) {

        User user = userRepository.findById(userId).
                orElseThrow(() -> new IllegalArgumentException("Usuário não encontrado"));

        if (emailRepository.existsByEmail(dto.getEmail())) {
            throw new IllegalArgumentException("Email já cadastrado");
        }

        if (dto.getIsPrimary()) {
            emailRepository.findByUserIdAndIsPrimaryTrue(userId)
                    .ifPresent(email -> {
                        email.setIsPrimary(false);
                        emailRepository.save(email);
                    });
        }

        UserEmail userEmail = assembler.toEntity(dto, user);

        UserEmail savedEmail = emailRepository.save(userEmail);

        return assembler.toModel(savedEmail);
    }

}
