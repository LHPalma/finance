package com.falizsh.finance.integrations.b3.adapter.impl;

import com.falizsh.finance.integrations.b3.adapter.BcbClient;
import com.falizsh.finance.integrations.b3.adapter.BcbSeries;
import com.falizsh.finance.integrations.b3.adapter.CdiRateLookupService;
import com.falizsh.finance.integrations.b3.adapter.dto.BcbSgsResponse;
import com.falizsh.finance.integrations.b3.adapter.dto.CdiRateInfoDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

import static com.falizsh.finance.infrastructure.utils.FinancialCalculous.calculateAnnualRate;

@Slf4j
@RequiredArgsConstructor
@Service
public class CdiRateProvider implements CdiRateLookupService {

    private final BcbClient client;

    @Override
    public CdiRateInfoDTO findLatestDailyRate() {
        try {

            List<BcbSgsResponse> response = client.getLastValue(BcbSeries.CDI.getCode(), 1);

            if (response != null && !response.isEmpty()) {
                BcbSgsResponse data = response.get(0);

                BigDecimal dailyRate = new BigDecimal(data.value());

                BigDecimal annualRate = calculateAnnualRate(dailyRate);

                return new CdiRateInfoDTO(
                        data.date(),
                        dailyRate,
                        annualRate
                );
            }
            return null;

        } catch (Exception e) {
            log.error("Erro ao consultar Taxa DI no BCB", e);
            return null;
        }
    }
}
