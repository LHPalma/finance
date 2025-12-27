package com.falizsh.finance.userTelephone.web;

import com.falizsh.finance.userTelephone.model.UserTelephone;
import com.falizsh.finance.userTelephone.repository.UserTelephoneQuery;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("v1/finance/user/{userId}/user-telephones")
public class UserTelephoneController {

    private final UserTelephoneQuery query;
    private final UserTelephoneAssemblerSupport assembler;

    @GetMapping
    public PagedModel<EntityModel<UserTelephone>> getAllUsersTelephones(
            Pageable pageable,
            PagedResourcesAssembler<UserTelephone> pagedAssembler
    ) {

        return pagedAssembler.toModel(query.findAll(pageable), assembler);

    }

}
