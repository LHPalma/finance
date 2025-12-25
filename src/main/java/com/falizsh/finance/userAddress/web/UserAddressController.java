package com.falizsh.finance.userAddress.web;


import com.falizsh.finance.userAddress.assembler.UserAddressAssemblerSuport;
import com.falizsh.finance.userAddress.dto.UserAddressCreateDTO;
import com.falizsh.finance.userAddress.model.UserAddress;
import com.falizsh.finance.userAddress.query.UserAddressQuery;
import com.falizsh.finance.userAddress.repository.UserAddressCommand;
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
@RequestMapping("v1/finance/user/{userId}/address")
public class UserAddressController {

    private final UserAddressQuery query;
    private final UserAddressCommand command;
    private final UserAddressAssemblerSuport assembler;

    @GetMapping
    public PagedModel<EntityModel<UserAddress>> findAll(
            Pageable pageable,
            PagedResourcesAssembler<UserAddress> pagedAssembler
    ) {

        return pagedAssembler.toModel(query.findAll(pageable), assembler);

    }

    @PostMapping
    public ResponseEntity<EntityModel<UserAddress>> create(
            @PathVariable Long userId,
            @RequestBody @Valid UserAddressCreateDTO data
    ) {
        UserAddress savedAddress = command.create(userId, data);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(assembler.toModel(savedAddress));
    }

}
