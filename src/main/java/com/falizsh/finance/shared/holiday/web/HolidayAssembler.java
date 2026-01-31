package com.falizsh.finance.shared.holiday.web;

import com.falizsh.finance.shared.holiday.projection.HolidayProjection;
import com.falizsh.finance.shared.holiday.projection.HolidayProjectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class HolidayAssembler implements RepresentationModelAssembler<HolidayProjection, EntityModel<HolidayProjectionModel>> {

    @Override
    public EntityModel<HolidayProjectionModel> toModel(HolidayProjection projection) {
        HolidayProjectionModel model = new HolidayProjectionModel(projection);

        model.add(linkTo(methodOn(HolidayController.class).getHolidays(
                projection.getDate(),
                projection.getDate(),
                projection.getCountryCode()
        )).withSelfRel());

        model.add(linkTo(methodOn(HolidayController.class).getHolidays(null, null, null))
                .withRel("holidays"));

        return EntityModel.of(model);
    }

}