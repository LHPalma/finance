package com.falizsh.finance.shared.web.assembler;

import com.falizsh.finance.shared.bcb.adapter.dto.CdiRateInfoDTO;
import com.falizsh.finance.shared.web.EconomicsRateLookupController;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class CdiAssembler implements RepresentationModelAssembler<CdiRateInfoDTO, EntityModel<CdiRateInfoDTO>> {

    @Override
    public EntityModel<CdiRateInfoDTO> toModel(CdiRateInfoDTO entity) {
        return EntityModel.of(entity,
            linkTo(methodOn(EconomicsRateLookupController.class).getCurrentCdiRate()).withSelfRel(),
            linkTo(methodOn(EconomicsRateLookupController.class).getCurrentSelicRate()).withRel("selic-rate"),
            linkTo(methodOn(EconomicsRateLookupController.class).getNextCopomMeeting()).withRel("next-copom-meeting")
        );
    }
}
