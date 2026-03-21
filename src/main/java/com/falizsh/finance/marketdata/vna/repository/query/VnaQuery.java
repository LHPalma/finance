package com.falizsh.finance.marketdata.vna.repository.query;

import com.falizsh.finance.marketdata.vna.model.Vna;

import java.time.LocalDate;
import java.util.List;

public interface VnaQuery {

    List<Object[]> findIdentifiersByDateRange(LocalDate startDate, LocalDate endDate);

    List<Vna> findByReferenceDate(LocalDate referenceDate);

}
