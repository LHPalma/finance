package com.falizsh.finance.marketdata.vna.web.graphql;

import com.falizsh.finance.marketdata.vna.model.VnaFetchStrategy;
import com.falizsh.finance.marketdata.vna.model.Vna;
import com.falizsh.finance.marketdata.vna.usecase.FetchVnaDataUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.time.LocalDate;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class VnaGraphQLAction {

    private final FetchVnaDataUseCase fetchVnaDataUseCase;

    @QueryMapping
    public List<Vna> vna(
            @Argument LocalDate date,
            @Argument VnaFetchStrategy strategy,
            @Argument Boolean ignoreCache
    ) {
        LocalDate targetDate = (date != null) ? date : LocalDate.now();
        boolean targetIgnoreCache = ignoreCache != null && ignoreCache;
        VnaFetchStrategy targetFetchStrategy = (strategy != null) ? strategy : VnaFetchStrategy.FALLBACK_AND_SAVE;

        return fetchVnaDataUseCase.execute(targetDate, strategy, targetIgnoreCache);
    }
}