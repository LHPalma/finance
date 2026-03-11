package com.falizsh.finance.marketdata.holiday.projection;

import com.falizsh.finance.marketdata.holiday.model.CountryCode;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import org.springframework.hateoas.server.core.Relation;

import java.time.LocalDate;

@Relation(collectionRelation = "holidays", itemRelation = "holiday")
@JsonPropertyOrder({"name", "date", "countryCode"})
public interface HolidayProjection {

    String getName();

    LocalDate getDate();

    CountryCode getCountryCode();

}
