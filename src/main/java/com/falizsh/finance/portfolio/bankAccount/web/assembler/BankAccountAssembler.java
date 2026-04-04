package com.falizsh.finance.portfolio.bankAccount.web.assembler;

import com.falizsh.finance.portfolio.bankAccount.dto.response.BankAccountResponse;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

@Component
public class BankAccountAssembler implements RepresentationModelAssembler<BankAccountResponse, EntityModel<BankAccountResponse>> {

    @Override
    public EntityModel<BankAccountResponse> toModel(BankAccountResponse response) {
        if (response == null) {
            return null;
        }

        return EntityModel.of(
                response
                // TODO: add self link pointing to getById when the endpoint is implemented.
        );
    }
}
