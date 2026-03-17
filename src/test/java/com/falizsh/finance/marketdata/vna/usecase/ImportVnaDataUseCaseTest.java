package com.falizsh.finance.marketdata.vna.usecase;

import com.falizsh.finance.infrastructure.sourcefile.action.AuditSourceFileAction;
import com.falizsh.finance.infrastructure.sourcefile.model.SourceFile;
import com.falizsh.finance.infrastructure.sourcefile.model.SourceFileDomain;
import com.falizsh.finance.integrations.anbima.adapter.dto.VnaResult;
import com.falizsh.finance.integrations.anbima.adapter.impl.AnbimaProvider;
import com.falizsh.finance.marketdata.vna.model.Vna;
import com.falizsh.finance.marketdata.vna.repository.command.VnaCommand;
import com.falizsh.finance.support.TestSupport;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

class ImportVnaDataUseCaseTest extends TestSupport {

    @Mock
    private AnbimaProvider anbimaProvider;

    @Mock
    private VnaCommand vnaCommand;

    @Mock
    private AuditSourceFileAction auditSourceFileAction;

    private ImportVnaDataUseCase importVnaDataUseCase;

    @Override
    public void init() {
        this.importVnaDataUseCase = new ImportVnaDataUseCase(anbimaProvider, vnaCommand, auditSourceFileAction);
    }

    @Test
    void shouldFailAuditAndReturnEmptyWhenProviderFails() {
        LocalDate date = LocalDate.of(2023, 10, 10);
        VnaResult mockResult = mock(VnaResult.class);
        SourceFile mockSourceFile = valid(SourceFile.class);

        when(mockResult.rawContent()).thenReturn(new byte[]{1, 2, 3});
        when(mockResult.isSuccess()).thenReturn(false);
        when(mockResult.failureReason()).thenReturn("API Offline");

        when(anbimaProvider.fetchVna(date)).thenReturn(mockResult);
        when(auditSourceFileAction.start(anyString(), any(byte[].class), anyString(), eq(SourceFileDomain.VNA))).thenReturn(mockSourceFile);

        List<Vna> result = importVnaDataUseCase.execute(date);

        assertThat(result).isEmpty();
        verify(auditSourceFileAction).fail(mockSourceFile, "API Offline");
        verifyNoInteractions(vnaCommand);
    }

    @Test
    void shouldCompleteAuditAndSaveWhenProviderSucceeds() {
        LocalDate date = LocalDate.of(2023, 10, 10);
        VnaResult mockResult = mock(VnaResult.class);
        SourceFile mockSourceFile = valid(SourceFile.class);
        Vna mockVna = valid(Vna.class);

        when(mockResult.rawContent()).thenReturn(null);
        when(mockResult.isSuccess()).thenReturn(true);
        when(mockResult.vnas()).thenReturn(List.of(mockVna));

        when(anbimaProvider.fetchVna(date)).thenReturn(mockResult);
        when(auditSourceFileAction.start(anyString(), any(byte[].class), anyString(), eq(SourceFileDomain.VNA))).thenReturn(mockSourceFile);
        when(vnaCommand.saveAllIgnoringDuplicates(anyList())).thenReturn(List.of(mockVna));

        List<Vna> result = importVnaDataUseCase.execute(date);

        assertThat(result).hasSize(1).containsExactly(mockVna);
        verify(auditSourceFileAction).complete(mockSourceFile);
        verify(vnaCommand).saveAllIgnoringDuplicates(anyList());
    }
}