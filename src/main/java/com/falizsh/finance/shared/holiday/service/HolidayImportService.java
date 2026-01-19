package com.falizsh.finance.shared.holiday.service;

import com.falizsh.finance.infra.sourcefile.model.SourceFile;
import com.falizsh.finance.infra.sourcefile.model.SourceFileDomain;
import com.falizsh.finance.infra.sourcefile.repository.SourceFileCommand;
import com.falizsh.finance.shared.holiday.adapter.HolidayProvider;
import com.falizsh.finance.shared.holiday.model.HolidayProviderResponse;
import com.falizsh.finance.shared.holiday.repository.HolidayCommand;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class HolidayImportService {

    private final HolidayCommand command;
    private final HolidayProvider provider;

    private final String EVERY_MONDAY_6_AM = "0 0 6 * * MON";
    private final SourceFileCommand sourceFileCommand;

    @Scheduled(cron = EVERY_MONDAY_6_AM)
    public void executeWeeklyImport() {
        log.info("Iniciando importação semanal de feriados.");

        HolidayProviderResponse response = provider.fetchHolidays();

        if (response.holidays().isEmpty()) {
            log.warn("Nenhum feriado encontrado na importação.");
            return;
        }

        SourceFile sourceFile = registerOriginalFile(response);

        int savedCount = command.saveAllIfNew(response.holidays());

        sourceFileCommand.complete(sourceFile.getId(), savedCount);

        log.info("Importação concluída. sourceFile ID: {}. Novos feriados salvos: {}", sourceFile, savedCount);
    }

    private SourceFile registerOriginalFile(HolidayProviderResponse response) {
        try {
            Map<String, Object> metadata = new HashMap<>();
            metadata.put("trigger", "scheduled_job");
            metadata.put("source", provider.getProviderName());
            metadata.put("total_items_fetched", response.holidays().size());

            return sourceFileCommand.create(
                    response.fileName(),
                    response.rawContent(),
                    response.contentType(),
                    SourceFileDomain.HOLIDAYS,
                    null,
                    metadata
            );

        } catch (Exception e) {
            log.error("Falha ao registrar SourceFile da importação web", e);
            throw new RuntimeException("Falha de auditoria na importação web", e);
        }

    }
}
