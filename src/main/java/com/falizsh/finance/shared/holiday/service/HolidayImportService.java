package com.falizsh.finance.shared.holiday.service;

import com.falizsh.finance.shared.holiday.adapter.HolidayProvider;
import com.falizsh.finance.shared.holiday.model.Holiday;
import com.falizsh.finance.shared.holiday.repository.HolidayCommand;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class HolidayImportService {

    private final HolidayCommand command;
    private final HolidayProvider provider;

    private final String EVERY_MONDAY_6_AM = "0 0 6 * * MON";

    @Scheduled(cron = EVERY_MONDAY_6_AM)
    public void executeWeeklyImport() {
        log.info("Iniciando importação semanal de feriados.");

        List<Holiday> holidays = provider.fetchHolidays();

        if (holidays.isEmpty()) {
            log.warn("Nenhum feriado encontrado na importação.");
            return;
        }

        int savedCount = command.saveAllIfNew(holidays);

        log.info("Importação concluída. Novos feriados salvos: {}", savedCount);
    }
}
