package com.falizsh.finance.marketdata.vna.usecase;

import com.falizsh.finance.marketdata.vna.model.Vna;
import com.falizsh.finance.marketdata.vna.repository.command.VnaCommand;
import com.falizsh.finance.support.TestSupport;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.*;

class ImportVnaDataRangeUseCaseTest extends TestSupport {

    @Mock
    private ImportVnaDataUseCase importVnaDataUseCase;

    @Mock
    private VnaCommand vnaCommand;

    private ImportVnaDataRangeUseCase importVnaDataRangeUseCase;

    @Override
    public void init() {
        this.importVnaDataRangeUseCase = new ImportVnaDataRangeUseCase(importVnaDataUseCase, vnaCommand);
    }

    @Test
    void shouldReturnEmptyListWhenStartDateIsFuture() {
        LocalDate startDate = LocalDate.now().plusDays(1);
        LocalDate endDate = LocalDate.now().plusDays(2);

        List<Vna> result = importVnaDataRangeUseCase.execute(startDate, endDate);

        assertThat(result).isEmpty();
        verifyNoInteractions(importVnaDataUseCase, vnaCommand);
    }

    @Test
    void shouldProcessRangeAndIgnoreExceptionsOnSpecificDays() {
        LocalDate startDate = LocalDate.now().minusDays(2);
        LocalDate endDate = LocalDate.now().minusDays(1);

        List<Vna> mockVnaList = valid(Vna.class, 2);

        when(importVnaDataUseCase.execute(startDate)).thenThrow(new RuntimeException("API Error"));
        when(importVnaDataUseCase.execute(endDate)).thenReturn(mockVnaList);

        when(vnaCommand.saveAllIgnoringDuplicates(anyList())).thenReturn(mockVnaList);

        List<Vna> result = importVnaDataRangeUseCase.execute(startDate, endDate);

        assertThat(result).hasSize(2).containsExactlyElementsOf(mockVnaList);
        verify(importVnaDataUseCase, times(1)).execute(startDate);
        verify(importVnaDataUseCase, times(1)).execute(endDate);
        verify(vnaCommand, times(1)).saveAllIgnoringDuplicates(anyList());
    }
}