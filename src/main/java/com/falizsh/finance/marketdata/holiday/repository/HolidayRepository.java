package com.falizsh.finance.marketdata.holiday.repository;

import com.falizsh.finance.marketdata.holiday.model.CountryCode;
import com.falizsh.finance.marketdata.holiday.model.Holiday;
import com.falizsh.finance.marketdata.holiday.projection.HolidayProjection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface HolidayRepository extends JpaRepository<Holiday, Long> {

    Optional<Holiday> findByDateAndCountryCode(LocalDate date, CountryCode countryCode);

    boolean existsByDateAndCountryCode(LocalDate date, CountryCode countryCode);

    List<HolidayProjection> findByCountryCodeAndDateBetween(CountryCode countryCode, LocalDate start, LocalDate end);

    Page<HolidayProjection> findByCountryCodeAndDateBetween(CountryCode countryCode, LocalDate start, LocalDate end, Pageable pageable);

}