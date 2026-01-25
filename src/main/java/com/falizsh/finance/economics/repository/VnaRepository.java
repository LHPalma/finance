package com.falizsh.finance.economics.repository;


import com.falizsh.finance.economics.model.Vna;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Optional;

@Repository
public interface VnaRepository extends JpaRepository<Vna, Long> {

    boolean existsBySelicCodeAndReferenceDate(String selicCode, LocalDate referenceDate);

    Optional<Vna> findBySelicCodeAndReferenceDate(String selicCode, LocalDate referenceDate);

}