package com.falizsh.finance.user.email.web;

import com.falizsh.finance.user.email.dto.UserEmailCreateDTO;
import com.falizsh.finance.user.email.dto.UserEmailResponseDTO;
import com.falizsh.finance.user.email.model.UserEmail;
import com.falizsh.finance.user.email.repository.UserEmailRepository;
import com.falizsh.finance.user.email.service.UserEmailService;
import jakarta.validation .Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("v1/finance/user/email")
public class UserEmailController {

    private final UserEmailRepository repository;
    private final UserEmailService service;

    @GetMapping
    public Page<UserEmail> findAllUsersEmails(Pageable pageable) {
        return repository.findAll(pageable);
    }

    @PostMapping("user/{userId}")
    public ResponseEntity<EntityModel<UserEmailResponseDTO>> saveEmailForUser(
            @PathVariable Long userId,
            @Valid @RequestBody UserEmailCreateDTO dto
    ) {
        EntityModel<UserEmailResponseDTO> savedUserEmail = service.create(userId, dto);

        return ResponseEntity.status(HttpStatus.CREATED).body(savedUserEmail);

    }

}
