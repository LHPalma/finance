package com.falizsh.finance.shared.web.assembler;

import com.falizsh.finance.economics.model.CopomMeeting;
import com.falizsh.finance.shared.web.EconomicsRateLookupController;
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
