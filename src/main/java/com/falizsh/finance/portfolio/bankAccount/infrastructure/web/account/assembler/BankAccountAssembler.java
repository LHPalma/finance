package com.falizsh.finance.portfolio.bankAccount.infrastructure.web.account.assembler;

import com.falizsh.finance.portfolio.bankAccount.application.dto.account.response.BankAccountResponse;
import com.falizsh.finance.portfolio.bankAccount.domain.model.account.BankAccountDetail;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

@Component
public class BankAccountAssembler
        implements RepresentationModelAssembler<BankAccountDetail, EntityModel<BankAccountResponse>> {

    @Override
    public EntityModel<BankAccountResponse> toModel(BankAccountDetail account) {
        BankAccountResponse response = BankAccountResponse.of()
                .id(account.getId())
                .name(account.getName())
                .description(account.getDescription())
                .type(account.getSystemType().getName())
                .overdraftLimit(account.getOverdraftLimit())
                .balance(account.getBalance())
                .currency(account.getCurrency())
                .build();

        // TODO: substituir por linkTo(methodOn(...).getById()) quando o endpoint existir.
        //       Débito técnico rastreado: GET /bank-accounts/{id}
        Link self = Link.of("/bank-accounts/" + account.getId()).withSelfRel();

        return EntityModel.of(response, self);
    }
}
