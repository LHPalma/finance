package com.falizsh.finance.shared.holiday.repository;

import com.falizsh.finance.shared.holiday.model.CountryCode;
import com.falizsh.finance.shared.holiday.model.Holiday;
import com.falizsh.finance.shared.holiday.projection.HolidayProjection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface HolidayQuery {

    List<Holiday> findAll();

    boolean isHoliday(LocalDate date, CountryCode countryCode);

    Optional<Holiday> findByDate(LocalDate date, CountryCode countryCode);

    List<HolidayProjection> findByRange(LocalDate startDate, LocalDate endDate, CountryCode countryCode);

    Page<HolidayProjection> findByRangePaginated(LocalDate startDate, LocalDate endDate, CountryCode countryCode, Pageable pageable);

}
