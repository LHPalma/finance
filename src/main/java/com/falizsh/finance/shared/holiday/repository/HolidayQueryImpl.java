package com.falizsh.finance.shared.holiday.repository;

import com.falizsh.finance.shared.holiday.model.CountryCode;
import com.falizsh.finance.shared.holiday.model.Holiday;
import com.falizsh.finance.shared.holiday.projection.HolidayProjection;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class HolidayQueryImpl implements HolidayQuery {

    private final HolidayRepository repository;

    @Override
    public List<Holiday> findAll() {
        return repository.findAll();
    }

    @Override
    public boolean isHoliday(LocalDate date, CountryCode countryCode) {
        return repository.existsByDateAndCountryCode(date, countryCode);
    }

    @Override
    public Optional<Holiday> findByDate(LocalDate date, CountryCode countryCode) {
        return repository.findByDateAndCountryCode(date, countryCode);
    }

    @Override
    public List<HolidayProjection> findByRange(LocalDate startDate, LocalDate endDate, CountryCode countryCode) {
        return repository.findByCountryCodeAndDateBetween(countryCode, startDate, endDate);
    }

    @Override
    public Page<HolidayProjection> findByRangePaginated(LocalDate startDate, LocalDate endDate, CountryCode countryCode, Pageable pageable) {
        return repository.findByCountryCodeAndDateBetween(countryCode, startDate, endDate, pageable);
    }
}
