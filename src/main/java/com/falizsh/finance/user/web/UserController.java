package com.falizsh.finance.user.web;

import com.falizsh.finance.user.assembler.UserAssembler;
import com.falizsh.finance.user.dto.UserCreateDTO;
import com.falizsh.finance.user.model.User;
import com.falizsh.finance.user.repository.UserCommand;
import com.falizsh.finance.user.repository.UserRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("v1/finance/user")
public class UserController {

    private final UserRepository repository;
    private final UserAssembler assembler;
    private final UserCommand command;


    @GetMapping
    public PagedModel<EntityModel<User>> findAll(
            Pageable pageable,
            PagedResourcesAssembler<User> pagedAssembler
    ) {
        return pagedAssembler.toModel(repository.findAll(pageable), assembler);
    }


    @GetMapping("id/{id}")
    public ResponseEntity<EntityModel<User>> findById(@PathVariable Long id) {
        return repository.findById(id)
                .map(assembler::toModel)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }


    @PostMapping
    public ResponseEntity<EntityModel<User>> saveUser(@Valid @RequestBody UserCreateDTO data) {

        User savedUser = command.create(data);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(assembler.toModel(savedUser));

    }

}
