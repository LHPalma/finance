package com.falizsh.finance.marketdata.vna.repository.projections;

import java.time.LocalDate;

public record VnaIdentifierData(String selicCode, LocalDate referenceDate) {
}
