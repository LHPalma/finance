package com.falizsh.finance.portfolio.bankAccount.infrastructure.web.transfer;

import com.falizsh.finance.portfolio.bankAccount.application.dto.transfer.request.TransferRequest;
import com.falizsh.finance.portfolio.bankAccount.application.dto.transfer.response.TransferResponse;
import com.falizsh.finance.portfolio.bankAccount.application.usecase.TransferBetweenAccountsUseCase;
import com.falizsh.finance.portfolio.bankAccount.infrastructure.web.transfer.assembler.TransferAssembler;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/transfers")
@RequiredArgsConstructor
public class TransferController {

    private final TransferBetweenAccountsUseCase transferUseCase;
    private final TransferAssembler assembler;

    @PostMapping
    public ResponseEntity<EntityModel<TransferResponse>> transfer(
            @RequestBody @Valid TransferRequest request
    ) {
        TransferResponse response = transferUseCase.execute(request);
        return ResponseEntity.ok(assembler.toModel(response));
    }
}
