package com.falizsh.finance.portfolio.bankAccount.infrastructure.web.transfer.assembler;

import com.falizsh.finance.portfolio.bankAccount.application.dto.transfer.response.TransferResponse;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

@Component
public class TransferAssembler
        implements RepresentationModelAssembler<TransferResponse, EntityModel<TransferResponse>> {

    @Override
    public EntityModel<TransferResponse> toModel(TransferResponse response) {
        // TODO: substituir por linkTo(methodOn(...).getById()) quando o endpoint existir.
        //       Débito técnico rastreado: GET /journal-entries/{id}
        Link self = Link.of("/journal-entries/" + response.journalEntryId()).withSelfRel();

        return EntityModel.of(response, self);
    }
}
