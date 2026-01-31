package com.falizsh.finance.shared.holiday.projection;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.core.Relation;

@Relation(collectionRelation = "holidays", itemRelation = "holiday")
public class HolidayProjectionModel extends EntityModel<HolidayProjection> {
    public HolidayProjectionModel(HolidayProjection holidayProjection) {
        super(holidayProjection);
    }
}
