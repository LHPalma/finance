package com.falizsh.finance.portfolio.bankAccount.web;

import com.falizsh.finance.portfolio.bankAccount.dto.request.CreateBankAccountRequest;
import com.falizsh.finance.portfolio.bankAccount.dto.response.BankAccountResponse;
import com.falizsh.finance.portfolio.bankAccount.mapper.BankAccountMapper;
import com.falizsh.finance.portfolio.bankAccount.model.BankAccount;
import com.falizsh.finance.portfolio.bankAccount.usecase.CreateBankAccountUseCase;
import com.falizsh.finance.portfolio.bankAccount.web.assembler.BankAccountAssembler;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/bank-accounts")
@RequiredArgsConstructor
public class BankAccountController {

    private final CreateBankAccountUseCase createUseCase;
    private final BankAccountMapper bankAccountMapper;
    private final BankAccountAssembler bankAccountAssembler;

    @PostMapping
    public ResponseEntity<EntityModel<BankAccountResponse>> create(
            @RequestBody @Valid CreateBankAccountRequest request
    ) {
        BankAccount account = createUseCase.execute(request);
        BankAccountResponse response = bankAccountMapper.toResponse(account);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/id/{id}").buildAndExpand(account.getId()).toUri();

        return ResponseEntity.created(uri).body(bankAccountAssembler.toModel(response));
    }
}
