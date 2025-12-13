package com.falizsh.finance.user.web;

import com.falizsh.finance.user.assembler.UserAssembler;
import com.falizsh.finance.user.model.User;
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

import java.time.LocalDateTime;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("v1/finance/user")
public class UserController {
    private final UserRepository repository;
    private final UserAssembler assembler;


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
    public ResponseEntity<?> saveUser(@Valid @RequestBody User user) {

        if (repository.existsByEmail(user.getEmail())) {
            return ResponseEntity
                    .status(HttpStatus.CONFLICT)
                    .body(Map.of(
                            "timestamp", LocalDateTime.now(),
                            "status", HttpStatus.CONFLICT.value(),
                            "error", HttpStatus.CONFLICT.getReasonPhrase(),
                            "Message", "Email já cadastrado."
                    ));
        }

        User savedUser = repository.save(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedUser);

    }

}
