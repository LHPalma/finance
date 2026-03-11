package com.falizsh.finance.integrations.web.assembler;

import com.falizsh.finance.marketdata.copomMeeting.model.CopomMeeting;
import com.falizsh.finance.integrations.web.EconomicsRateLookupController;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class CopomMeetingAssembler implements RepresentationModelAssembler<CopomMeeting, EntityModel<CopomMeeting>> {
    @Override
    public EntityModel<CopomMeeting> toModel(CopomMeeting entity) {
        return EntityModel.of(entity,
                linkTo(methodOn(EconomicsRateLookupController.class).getNextCopomMeeting()).withSelfRel()
        );
    }
}
