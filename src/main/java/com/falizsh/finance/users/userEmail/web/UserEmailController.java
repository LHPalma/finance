package com.falizsh.finance.users.userEmail.web;

import com.falizsh.finance.users.userEmail.assembler.UserEmailAssembler;
import com.falizsh.finance.users.userEmail.dto.UserEmailCreateDTO;
import com.falizsh.finance.users.userEmail.dto.UserEmailResponseDTO;
import com.falizsh.finance.users.userEmail.model.UserEmail;
import com.falizsh.finance.users.userEmail.repository.UserEmailCommand;
import com.falizsh.finance.users.userEmail.repository.UserEmailQuery;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("v1/finance/user/email")
public class UserEmailController {

    private final UserEmailQuery query;
    private final UserEmailCommand command;
    private final UserEmailAssembler assembler;

    @GetMapping
    public Page<UserEmail> findAllUsersEmails(Pageable pageable) {
        return query.findAll(pageable);
    }

    @GetMapping("user/{userId}")
    public PagedModel<EntityModel<UserEmailResponseDTO>> findAllByUserId(
            @PathVariable Long userId,
            Pageable pageable,
            PagedResourcesAssembler<UserEmail> pagedAssembler
    ) {
        return pagedAssembler.toModel(query.findAllByUserId(userId, pageable), assembler);
    }

    @PostMapping("user/{userId}")
    public ResponseEntity<EntityModel<UserEmailResponseDTO>> saveEmailForUser(
            @PathVariable Long userId,
            @Valid @RequestBody UserEmailCreateDTO dto
    ) {
        UserEmail savedUserEmail = command.create(userId, dto);

        return ResponseEntity.status(HttpStatus.CREATED).body(assembler.toModel(savedUserEmail));
    }

}
