package com.falizsh.finance.marketdata.vna.repository;

import com.falizsh.finance.marketdata.vna.repository.projections.VnaIdentifierData;

import java.time.LocalDate;
import java.util.List;

public interface VnaRepositoryCustom {

    List<VnaIdentifierData> findIdentifiersByDateRange(LocalDate startDate, LocalDate endDate);

}
