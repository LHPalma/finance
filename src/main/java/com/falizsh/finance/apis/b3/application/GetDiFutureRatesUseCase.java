package com.falizsh.finance.apis.b3.application;

import com.falizsh.finance.apis.b3.application.dto.DiFutureResult;

import java.time.LocalDate;
import java.util.List;

public interface GetDiFutureRatesUseCase {

    List<DiFutureResult> execute();

    List<DiFutureResult> execute(LocalDate targetDate);

}
