package com.falizsh.finance.marketdata.vna.repository.query;

import java.time.LocalDate;
import java.util.List;

public interface VnaQuery {

    List<Object[]> findIdentifiersByDateRange(LocalDate startDate, LocalDate endDate);

}
