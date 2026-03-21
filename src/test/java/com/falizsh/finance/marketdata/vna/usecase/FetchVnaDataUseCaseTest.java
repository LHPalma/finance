package com.falizsh.finance.marketdata.vna.usecase;

import com.falizsh.finance.infrastructure.cache.port.CacheProvider;
import com.falizsh.finance.integrations.anbima.adapter.dto.VnaResult;
import com.falizsh.finance.integrations.anbima.adapter.impl.AnbimaProvider;
import com.falizsh.finance.marketdata.vna.model.VnaFetchStrategy;
import com.falizsh.finance.marketdata.vna.model.Vna;
import com.falizsh.finance.marketdata.vna.repository.query.VnaQuery;
import com.falizsh.finance.support.TestSupport;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.function.Supplier;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class FetchVnaDataUseCaseTest extends TestSupport {

    @Mock
    private VnaQuery vnaQuery;

    @Mock
    private AnbimaProvider anbimaProvider;

    @Mock
    private ImportVnaDataUseCase importVnaDataUseCase;

    @Mock
    private CacheProvider cacheProvider;

    private FetchVnaDataUseCase fetchVnaDataUseCase;

    @Override
    public void init() {
        this.fetchVnaDataUseCase = new FetchVnaDataUseCase(
                anbimaProvider,
                importVnaDataUseCase,
                vnaQuery,
                cacheProvider
        );

        when(cacheProvider.getOrFetch(anyString(), anyBoolean(), any(), any()))
                .thenAnswer(invocation -> {
                    Supplier<List<Vna>> fetcherLambda = invocation.getArgument(3);
                    return fetcherLambda.get();
                });
    }

    @Test
    void shouldReturnEmptyListWhenDateIsFuture() {
        LocalDate futureDate = LocalDate.now().plusDays(1);

        List<Vna> result = fetchVnaDataUseCase.execute(futureDate, VnaFetchStrategy.FALLBACK_AND_SAVE, false);

        assertThat(result).isEmpty();
        verifyNoInteractions(cacheProvider, vnaQuery, anbimaProvider, importVnaDataUseCase);
    }

    @Test
    void shouldReturnEmptyListWhenDateIsNull() {
        List<Vna> result = fetchVnaDataUseCase.execute(null, VnaFetchStrategy.FALLBACK_AND_SAVE, false);

        assertThat(result).isEmpty();
        verifyNoInteractions(cacheProvider, vnaQuery, anbimaProvider, importVnaDataUseCase);
    }

    @Test
    void shouldExecuteLocalOnlyStrategy() {
        LocalDate validDate = LocalDate.now().minusDays(1);
        Vna mockVna = valid(Vna.class);

        when(vnaQuery.findByReferenceDate(validDate)).thenReturn(List.of(mockVna));

        List<Vna> result = fetchVnaDataUseCase.execute(validDate, VnaFetchStrategy.LOCAL_ONLY, true);

        assertThat(result).hasSize(1).containsExactly(mockVna);
        verify(vnaQuery).findByReferenceDate(validDate);
        verifyNoInteractions(anbimaProvider, importVnaDataUseCase);
    }

    @Test
    void shouldExecuteRemoteOnlyStrategy() {
        LocalDate validDate = LocalDate.now().minusDays(1);
        Vna mockVna = valid(Vna.class);
        VnaResult mockResult = mock(VnaResult.class);

        when(mockResult.vnas()).thenReturn(List.of(mockVna));
        when(anbimaProvider.fetchVna(validDate)).thenReturn(mockResult);

        List<Vna> result = fetchVnaDataUseCase.execute(validDate, VnaFetchStrategy.REMOTE_ONLY, false);

        assertThat(result).hasSize(1).containsExactly(mockVna);
        verify(anbimaProvider).fetchVna(validDate);
        verifyNoInteractions(vnaQuery, importVnaDataUseCase);
    }

    @Test
    void shouldExecuteFallbackNoSaveAndReturnLocalWhenFound() {
        LocalDate validDate = LocalDate.now().minusDays(1);
        Vna mockVna = valid(Vna.class);

        when(vnaQuery.findByReferenceDate(validDate)).thenReturn(List.of(mockVna));

        List<Vna> result = fetchVnaDataUseCase.execute(validDate, VnaFetchStrategy.FALLBACK_NO_SAVE, false);

        assertThat(result).hasSize(1).containsExactly(mockVna);
        verify(vnaQuery).findByReferenceDate(validDate);
        verifyNoInteractions(anbimaProvider, importVnaDataUseCase);
    }

    @Test
    void shouldExecuteFallbackNoSaveAndReturnRemoteWhenLocalNotFound() {
        LocalDate validDate = LocalDate.now().minusDays(1);
        Vna mockVna = valid(Vna.class);
        VnaResult mockResult = mock(VnaResult.class);

        when(vnaQuery.findByReferenceDate(validDate)).thenReturn(Collections.emptyList());
        when(mockResult.vnas()).thenReturn(List.of(mockVna));
        when(anbimaProvider.fetchVna(validDate)).thenReturn(mockResult);

        List<Vna> result = fetchVnaDataUseCase.execute(validDate, VnaFetchStrategy.FALLBACK_NO_SAVE, false);

        assertThat(result).hasSize(1).containsExactly(mockVna);
        verify(vnaQuery).findByReferenceDate(validDate);
        verify(anbimaProvider).fetchVna(validDate);
        verifyNoInteractions(importVnaDataUseCase);
    }

    @Test
    void shouldExecuteFallbackAndSaveWhenLocalNotFound() {
        LocalDate validDate = LocalDate.now().minusDays(1);
        Vna mockVna = valid(Vna.class);

        when(vnaQuery.findByReferenceDate(validDate)).thenReturn(Collections.emptyList());
        when(importVnaDataUseCase.execute(validDate)).thenReturn(List.of(mockVna));

        List<Vna> result = fetchVnaDataUseCase.execute(validDate, VnaFetchStrategy.FALLBACK_AND_SAVE, true);

        assertThat(result).hasSize(1).containsExactly(mockVna);
        verify(vnaQuery).findByReferenceDate(validDate);
        verify(importVnaDataUseCase).execute(validDate);
        verifyNoInteractions(anbimaProvider);
    }
}