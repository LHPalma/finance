package com.falizsh.finance.marketdata.vna.repository;


import com.falizsh.finance.marketdata.vna.model.Vna;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;


@Repository
public interface VnaRepository extends JpaRepository<Vna, Long>, VnaRepositoryCustom {

    boolean existsBySelicCodeAndReferenceDate(String selicCode, LocalDate referenceDate);

    Optional<Vna> findBySelicCodeAndReferenceDate(String selicCode, LocalDate referenceDate);

    List<Vna> findByReferenceDate(LocalDate date);

}