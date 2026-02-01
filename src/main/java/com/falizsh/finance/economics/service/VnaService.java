package com.falizsh.finance.economics.service;


import com.falizsh.finance.economics.adpater.dto.VnaResult;
import com.falizsh.finance.economics.adpater.impl.AnbimaProvider;
import com.falizsh.finance.economics.model.Vna;
import com.falizsh.finance.economics.repository.VnaCommand;
import com.falizsh.finance.infra.sourcefile.model.SourceFile;
import com.falizsh.finance.infra.sourcefile.model.SourceFileDomain;
import com.falizsh.finance.infra.sourcefile.model.SourceFileStatus;
import com.falizsh.finance.infra.sourcefile.repository.SourceFileCommand;
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
public class VnaService {

    private final AnbimaProvider anbimaProvider;

    private final SourceFileCommand sourceFileCommand;

    private final VnaCommand vnaCommand;


    public List<Vna> importData(LocalDate date) {
        VnaResult result = anbimaProvider.fetchVna(date);

        byte[] contentToSave = result.rawContent() != null ? result.rawContent() : new byte[0];

        SourceFile sourceFile = sourceFileCommand.create(
                "vna_" + date + ".xml",
                contentToSave,
                "application/xml",
                SourceFileDomain.VNA,
                null,
                null
        );

        if (!result.isSuccess()) {
            sourceFileCommand.addError(sourceFile, result.failureReason(), null);
            sourceFileCommand.updateStatus(sourceFile, SourceFileStatus.FAILED);
            return Collections.emptyList();
        }

        List<Vna> savedVnas = vnaCommand.saveAllIgnoringDuplicates(result.vnas());

        sourceFileCommand.updateStatus(sourceFile, SourceFileStatus.PROCESSED);

        return savedVnas;

    }

    public List<Vna> fetchVna(LocalDate date) {
        if (!validateDate(date)) {
            return Collections.emptyList();
        }

        return anbimaProvider.fetchVna(date).vnas();
    }

    @Transactional
    public List<Vna> importRange(LocalDate startDate, LocalDate endDate) {
        if (!validateDate(startDate) || !validateDate(endDate)) {
            return Collections.emptyList();
        }

        List<Vna> fetchedVna = new ArrayList<>();

        startDate.datesUntil(endDate.plusDays(1)).forEach(date -> {
            try {
                List<Vna> dailyVna = importData(date);
                fetchedVna.addAll(dailyVna);
            } catch (Exception e) {
                log.error("Erro ao processar data {} no range: {}", date, e.getMessage());
            }
        });

        return vnaCommand.saveAllIgnoringDuplicates(fetchedVna);

    }

    private boolean validateDate(LocalDate date) {
        return (date != null && !date.isAfter(LocalDate.now()));
    }
}
