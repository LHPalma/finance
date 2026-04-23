package com.falizsh.finance.portfolio.bankAccount.infrastructure.web.type;

import com.falizsh.finance.portfolio.bankAccount.application.dto.type.response.SystemAccountTypeResponse;
import com.falizsh.finance.portfolio.bankAccount.application.query.type.FetchAllActiveSystemAccountTypesHandler;
import com.falizsh.finance.portfolio.bankAccount.application.query.type.FetchAllActiveSystemAccountTypesQuery;
import com.falizsh.finance.portfolio.bankAccount.application.query.type.FetchSystemAccountTypeByIdHandler;
import com.falizsh.finance.portfolio.bankAccount.application.query.type.FetchSystemAccountTypeByIdQuery;
import com.falizsh.finance.portfolio.bankAccount.domain.model.type.SystemAccountType;
import com.falizsh.finance.portfolio.bankAccount.infrastructure.web.type.assembler.SystemAccountTypeAssembler;
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
@RequestMapping("/bank-account/system-account-type")
public class SystemAccountTypeController {

    private final FetchSystemAccountTypeByIdHandler fetchByIdHandler;
    private final FetchAllActiveSystemAccountTypesHandler fetchAllHandler;
    private final SystemAccountTypeAssembler assembler;

    @GetMapping("/id/{id}")
    public ResponseEntity<EntityModel<SystemAccountTypeResponse>> getById(@PathVariable long id) {
        SystemAccountType type = fetchByIdHandler.handle(new FetchSystemAccountTypeByIdQuery(id));
        return ResponseEntity.ok(assembler.toModel(type));
    }

    @GetMapping
    public CollectionModel<EntityModel<SystemAccountTypeResponse>> getAllSystemAccountTypes() {
        List<SystemAccountType> types = fetchAllHandler.handle(new FetchAllActiveSystemAccountTypesQuery());

        CollectionModel<EntityModel<SystemAccountTypeResponse>> collectionModel =
                assembler.toCollectionModel(types);

        collectionModel.add(
                linkTo(methodOn(SystemAccountTypeController.class).getAllSystemAccountTypes()).withSelfRel()
        );

        return collectionModel;
    }
}
