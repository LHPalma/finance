package com.falizsh.finance.marketdata.vna.usecase;

import com.falizsh.finance.integrations.anbima.adapter.dto.VnaResult;
import com.falizsh.finance.integrations.anbima.adapter.impl.AnbimaProvider;
import com.falizsh.finance.marketdata.vna.model.Vna;
import com.falizsh.finance.support.TestSupport;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class FetchVnaDataUseCaseTest extends TestSupport {

    @Mock
    private AnbimaProvider anbimaProvider;

    private FetchVnaDataUseCase fetchVnaDataUseCase;

    @Override
    public void init() {
        this.fetchVnaDataUseCase = new FetchVnaDataUseCase(anbimaProvider);
    }

    @Test
    void shouldReturnEmptyListWhenDateIsFuture() {
        LocalDate futureDate = LocalDate.now().plusDays(1);

        List<Vna> result = fetchVnaDataUseCase.execute(futureDate);

        assertThat(result).isEmpty();
        verifyNoInteractions(anbimaProvider);
    }

    @Test
    void shouldReturnEmptyListWhenDateIsNull() {
        List<Vna> result = fetchVnaDataUseCase.execute(null);

        assertThat(result).isEmpty();
        verifyNoInteractions(anbimaProvider);
    }

    @Test
    void shouldReturnVnaListWhenDateIsValid() {
        LocalDate validDate = LocalDate.now().minusDays(1);
        Vna mockVna = valid(Vna.class);
        VnaResult mockResult = mock(VnaResult.class);

        when(mockResult.vnas()).thenReturn(List.of(mockVna));
        when(anbimaProvider.fetchVna(validDate)).thenReturn(mockResult);

        List<Vna> result = fetchVnaDataUseCase.execute(validDate);

        assertThat(result).hasSize(1).containsExactly(mockVna);
        verify(anbimaProvider, times(1)).fetchVna(validDate);
    }
}