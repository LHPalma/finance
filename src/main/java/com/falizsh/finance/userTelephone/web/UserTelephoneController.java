package com.falizsh.finance.userTelephone.web;

import com.falizsh.finance.userTelephone.dto.UserTelephoneCreateRequest;
import com.falizsh.finance.userTelephone.model.UserTelephone;
import com.falizsh.finance.userTelephone.repository.UserTelephoneCommand;
import com.falizsh.finance.userTelephone.repository.UserTelephoneQuery;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("v1/finance/user/{userId}/user-telephones")
public class UserTelephoneController {

    private final UserTelephoneQuery query;
    private final UserTelephoneAssemblerSupport assembler;
    private final UserTelephoneCommand command;

    @GetMapping
    public PagedModel<EntityModel<UserTelephone>> getAllUsersTelephones(
            Pageable pageable,
            PagedResourcesAssembler<UserTelephone> pagedAssembler
    ) {

        return pagedAssembler.toModel(query.findAll(pageable), assembler);

    }

    @PostMapping
    public ResponseEntity<EntityModel<UserTelephone>> createUserTelephone(
            @RequestBody @Valid UserTelephoneCreateRequest request,
            @PathVariable Long userId
    ) {

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(assembler.toModel(command.create(request, userId)));
    }

}
