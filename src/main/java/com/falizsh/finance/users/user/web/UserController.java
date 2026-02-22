package com.falizsh.finance.users.user.web;

import com.falizsh.finance.users.user.assembler.UserAssembler;
import com.falizsh.finance.users.user.dto.UserCreateDTO;
import com.falizsh.finance.users.user.dto.response.UserResponse;
import com.falizsh.finance.users.user.model.User;
import com.falizsh.finance.users.user.repository.UserCommand;
import com.falizsh.finance.users.user.repository.UserQuery;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequiredArgsConstructor
@RequestMapping("v1/finance/user")
public class UserController {

    private final UserAssembler assembler;
    private final UserQuery query;
    private final UserCommand command;


    @GetMapping
    public PagedModel<EntityModel<UserResponse>> findAll(
            Pageable pageable,
            PagedResourcesAssembler<User> pagedAssembler
    ) {
        return pagedAssembler.toModel(query.findAll(pageable), assembler);
    }


    @GetMapping("id/{id}")
    public ResponseEntity<EntityModel<UserResponse>> findById(@PathVariable Long id) {
        return ResponseEntity.ok(
                assembler.toModel(
                        query.findById(id)
                )
        );
    }


    @PostMapping
    public ResponseEntity<EntityModel<UserResponse>> saveUser(@Valid @RequestBody UserCreateDTO data) {

        User savedUser = command.create(data);

        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/id/{id}").buildAndExpand(savedUser.getId()).toUri();

        return ResponseEntity.created(uri).body(assembler.toModel(savedUser));

    }

}
