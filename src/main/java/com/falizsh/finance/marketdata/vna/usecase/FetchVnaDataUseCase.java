package com.falizsh.finance.marketdata.vna.usecase;

import com.falizsh.finance.integrations.anbima.adapter.impl.AnbimaProvider;
import com.falizsh.finance.marketdata.vna.model.Vna;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

@RequiredArgsConstructor
@Service
public class FetchVnaDataUseCase {

    private final AnbimaProvider anbimaProvider;

    public List<Vna> execute(LocalDate date) {
        if (!validateDate(date)) {
            return Collections.emptyList();
        }

        return anbimaProvider.fetchVna(date).vnas();
    }

    private boolean validateDate(LocalDate date) {
        return (date != null && !date.isAfter(LocalDate.now()));
    }
}