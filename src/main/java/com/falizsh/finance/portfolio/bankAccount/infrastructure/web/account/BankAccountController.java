package com.falizsh.finance.portfolio.bankAccount.infrastructure.web.account;

import com.falizsh.finance.portfolio.bankAccount.application.dto.account.response.BankAccountResponse;
import com.falizsh.finance.portfolio.bankAccount.application.usecase.CreateBankAccountUseCase;
import com.falizsh.finance.portfolio.bankAccount.application.dto.account.request.CreateBankAccountRequest;
import com.falizsh.finance.portfolio.bankAccount.domain.model.account.BankAccountDetail;
import com.falizsh.finance.portfolio.bankAccount.infrastructure.web.account.assembler.BankAccountAssembler;
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
    private final BankAccountAssembler assembler;

    @PostMapping
    public ResponseEntity<EntityModel<BankAccountResponse>> create(
            @RequestBody @Valid CreateBankAccountRequest request
    ) {
        BankAccountDetail account = createUseCase.execute(request);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(account.getId())
                .toUri();

        return ResponseEntity.created(uri).body(assembler.toModel(account));
    }
}
