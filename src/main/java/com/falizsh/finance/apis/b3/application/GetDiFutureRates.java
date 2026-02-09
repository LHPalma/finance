package com.falizsh.finance.apis.b3.application;

import com.falizsh.finance.apis.b3.application.dto.DiFutureResult;
import com.falizsh.finance.apis.b3.application.port.DiFutureGateway;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class GetDiFutureRates implements GetDiFutureRatesUseCase {

    private final DiFutureGateway diFutureGateway;

    @Override
    public List<DiFutureResult> execute() {
        return diFutureGateway.getSettlementPrices();
    }

    @Override
    public List<DiFutureResult> execute(LocalDate targetDate) {

        if (targetDate == null) {
            throw new IllegalArgumentException("targetDate.must.not.be.null");
        }

        List<DiFutureResult> fullCurve = diFutureGateway.getSettlementPrices();

        if (fullCurve.isEmpty()) {
            return Collections.emptyList();
        }

        TreeMap<LocalDate, DiFutureResult> map = fullCurve.stream()
                .collect(Collectors.toMap(
                        DiFutureResult::maturityDate,
                        Function.identity(),
                        (existing, replacement) -> existing,
                        TreeMap::new
                ));

        if (map.containsKey(targetDate)) {
            return List.of(map.get(targetDate));
        }

        List<DiFutureResult> neighbors = new ArrayList<>();

        Map.Entry<LocalDate, DiFutureResult> floor = map.floorEntry(targetDate);
        Map.Entry<LocalDate, DiFutureResult> ceiling = map.ceilingEntry(targetDate);

        if (floor != null) {
            neighbors.add(floor.getValue());
        }
        if (ceiling != null) {
            neighbors.add(ceiling.getValue());
        }

        return neighbors;
    }
}
