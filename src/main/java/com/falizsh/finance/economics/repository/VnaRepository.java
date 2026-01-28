package com.falizsh.finance.economics.repository;


import com.falizsh.finance.economics.model.Vna;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static com.falizsh.finance.economics.repository.VnaQueries.FIND_IDENTIFIERS_BY_DATE_RANGE_QUERY;

@Repository
public interface VnaRepository extends JpaRepository<Vna, Long> {

    boolean existsBySelicCodeAndReferenceDate(String selicCode, LocalDate referenceDate);

    Optional<Vna> findBySelicCodeAndReferenceDate(String selicCode, LocalDate referenceDate);

    @Query(value = FIND_IDENTIFIERS_BY_DATE_RANGE_QUERY, nativeQuery = true)
    List<Object[]> findIdentifiersByDateRange(LocalDate startDate, LocalDate endDate);

}