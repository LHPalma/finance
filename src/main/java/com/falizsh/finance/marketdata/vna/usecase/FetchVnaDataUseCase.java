package com.falizsh.finance.marketdata.vna.usecase;

import com.falizsh.finance.infrastructure.cache.port.CacheProvider;
import com.falizsh.finance.integrations.anbima.adapter.impl.AnbimaProvider;
import com.falizsh.finance.marketdata.vna.model.Vna;
import com.falizsh.finance.marketdata.vna.model.VnaFetchStrategy;
import com.falizsh.finance.marketdata.vna.repository.query.VnaQuery;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

@RequiredArgsConstructor
@Service
public class FetchVnaDataUseCase {

    private final AnbimaProvider anbimaProvider;
    private final ImportVnaDataUseCase importVnaDataUseCase;
    private final VnaQuery vnaQuery;
    private final CacheProvider cacheProvider;

    private static final String CACHE_KEY_PREFIX = "vna::";
    private static final Duration CACHE_TTL = Duration.ofHours(12);

    public List<Vna> execute(LocalDate date, VnaFetchStrategy strategy, boolean ignoreCache) {
        if (!validateDate(date)) {
            return Collections.emptyList();
        }

        String cacheKey = CACHE_KEY_PREFIX + date;

        return cacheProvider.getOrFetch(cacheKey, ignoreCache, CACHE_TTL, () -> switch (strategy) {
            case LOCAL_ONLY -> fetchLocal(date);
            case REMOTE_ONLY -> fetchRemote(date);
            case FALLBACK_NO_SAVE -> fetchFallbackNoSave(date);
            case FALLBACK_AND_SAVE -> fetchFallbackAndSave(date);
        });
    }

    private List<Vna> fetchLocal(LocalDate date) {
        List<Vna> localData = vnaQuery.findByReferenceDate(date);
        return (localData != null) ? localData : Collections.emptyList();
    }

    private List<Vna> fetchRemote(LocalDate date) {
        var anbimaResponse = anbimaProvider.fetchVna(date);
        return (anbimaResponse != null && anbimaResponse.vnas() != null)
                ? anbimaResponse.vnas()
                : Collections.emptyList();
    }

    private List<Vna> fetchFallbackNoSave(LocalDate date) {
        List<Vna> localData = fetchLocal(date);
        if (!localData.isEmpty()) {
            return localData;
        }
        return fetchRemote(date);
    }

    private List<Vna> fetchFallbackAndSave(LocalDate date) {
        List<Vna> localData = fetchLocal(date);
        if (!localData.isEmpty()) {
            return localData;
        }
        return importVnaDataUseCase.execute(date);
    }

    private boolean validateDate(LocalDate date) {
        return (date != null && !date.isAfter(LocalDate.now()));
    }
}