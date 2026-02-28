package com.falizsh.finance.bankAccount.systemAccountType.web;

import com.falizsh.finance.bankAccount.systemAccountType.dto.response.SystemAccountTypeResponse;
import com.falizsh.finance.bankAccount.systemAccountType.model.SystemAccountType;
import com.falizsh.finance.bankAccount.systemAccountType.repository.SystemAccountTypeQuery;
import com.falizsh.finance.bankAccount.systemAccountType.web.assembler.SystemAccountTypeAssembler;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RequiredArgsConstructor
@RestController
@RequestMapping("/v1/finance/bank-account/system-account-type")
public class SystemAccountTypeController {

    private final SystemAccountTypeQuery query;
    private final SystemAccountTypeAssembler assembler;

    @GetMapping("/id/{id}")
    public ResponseEntity<EntityModel<SystemAccountTypeResponse>> getById(@PathVariable long id) {
        SystemAccountType type = query.findById(id);

        return ResponseEntity.ok(
                assembler.toModel(type)
        );
    }

    @GetMapping
    public CollectionModel<EntityModel<SystemAccountTypeResponse>> getAllSystemAccountTypes() {

        List<SystemAccountType> types = query.findAllActiveSystemAccountTypes();

        CollectionModel<EntityModel<SystemAccountTypeResponse>> collectionModel = assembler.toCollectionModel(types);

        collectionModel.add(linkTo(methodOn(SystemAccountTypeController.class).getAllSystemAccountTypes()).withSelfRel());

        return collectionModel;
    }

}
