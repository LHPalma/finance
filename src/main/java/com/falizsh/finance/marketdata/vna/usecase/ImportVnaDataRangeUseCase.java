package com.falizsh.finance.marketdata.vna.usecase;

import com.falizsh.finance.marketdata.vna.model.Vna;
import com.falizsh.finance.marketdata.vna.repository.command.VnaCommand;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class ImportVnaDataRangeUseCase {

    private final ImportVnaDataUseCase importVnaDataUseCase;
    private final VnaCommand vnaCommand;

    @Transactional
    public List<Vna> execute(LocalDate startDate, LocalDate endDate) {
        if (validateDate(startDate) || validateDate(endDate)) {
            return Collections.emptyList();
        }

        List<Vna> fetchedVna = new ArrayList<>();

        startDate.datesUntil(endDate.plusDays(1)).forEach(date -> {
            try {
                List<Vna> dailyVna = importVnaDataUseCase.execute(date);
                fetchedVna.addAll(dailyVna);
            } catch (Exception e) {
                log.error("Erro ao processar data {} no range: {}", date, e.getMessage());
            }
        });

        return vnaCommand.saveAllIgnoringDuplicates(fetchedVna);
    }

    private boolean validateDate(LocalDate date) {
        return (date == null || date.isAfter(LocalDate.now()));
    }
}