package com.falizsh.finance.marketdata.vna.usecase;

import com.falizsh.finance.integrations.anbima.adapter.dto.VnaResult;
import com.falizsh.finance.integrations.anbima.adapter.impl.AnbimaProvider;
import com.falizsh.finance.marketdata.vna.model.Vna;
import com.falizsh.finance.marketdata.vna.repository.command.VnaCommand;
import com.falizsh.finance.infrastructure.sourcefile.action.AuditSourceFileAction; // <-- Action Injetada
import com.falizsh.finance.infrastructure.sourcefile.model.SourceFile;
import com.falizsh.finance.infrastructure.sourcefile.model.SourceFileDomain;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

@RequiredArgsConstructor
@Service
public class ImportVnaDataUseCase {

    private final AnbimaProvider anbimaProvider;
    private final VnaCommand vnaCommand;
    private final AuditSourceFileAction auditSourceFileAction;

    public List<Vna> execute(LocalDate date) {
        VnaResult result = anbimaProvider.fetchVna(date);
        byte[] contentToSave = result.rawContent() != null ? result.rawContent() : new byte[0];

        SourceFile sourceFile = auditSourceFileAction.start(
                "vna_" + date + ".xml",
                contentToSave,
                "application/xml",
                SourceFileDomain.VNA
        );

        if (!result.isSuccess()) {
            auditSourceFileAction.fail(sourceFile, result.failureReason());
            return Collections.emptyList();
        }

        List<Vna> savedVnas = vnaCommand.saveAllIgnoringDuplicates(result.vnas());
        auditSourceFileAction.complete(sourceFile);

        return savedVnas;
    }
}