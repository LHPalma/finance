package com.falizsh.finance.shared.bcb.adapter.impl;

import com.falizsh.finance.shared.bcb.adapter.BcbClient;
import com.falizsh.finance.shared.bcb.adapter.BcbSeries;
import com.falizsh.finance.shared.bcb.adapter.CdiRateLookupService;
import com.falizsh.finance.shared.bcb.adapter.dto.BcbSgsResponse;
import com.falizsh.finance.shared.bcb.adapter.dto.CdiRateInfoDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

import static com.falizsh.finance.shared.utils.FinancialCalculous.calculateAnnualRate;

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
