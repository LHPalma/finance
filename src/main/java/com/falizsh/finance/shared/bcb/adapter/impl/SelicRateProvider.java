package com.falizsh.finance.shared.bcb.adapter.impl;

import com.falizsh.finance.shared.bcb.adapter.BcbClient;
import com.falizsh.finance.shared.bcb.adapter.BcbSeries;
import com.falizsh.finance.shared.bcb.adapter.SelicRateLookupService;
import com.falizsh.finance.shared.bcb.adapter.dto.BcbSgsResponse;
import com.falizsh.finance.shared.bcb.adapter.dto.SelicRateInfoDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

import static com.falizsh.finance.shared.utils.FinancialCalculous.calculateAnnualRate;

@Slf4j
@RequiredArgsConstructor
@Service
public class SelicRateProvider implements SelicRateLookupService {

    private final BcbClient client;

    @Override
    public SelicRateInfoDTO findLatestDailyRate() {
        try {

            List<BcbSgsResponse> response = client.getLastValue(BcbSeries.SELIC.getCode(), 1);

            if (response != null && !response.isEmpty()) {
                BcbSgsResponse data = response.get(0);

                BigDecimal dailyRate = new BigDecimal(data.value());

                BigDecimal annualRate = calculateAnnualRate(dailyRate);

                return new SelicRateInfoDTO(
                        data.date(),
                        dailyRate,
                        annualRate
                );
            }
            return null;

        } catch (Exception e) {
            log.error("Erro ao consultar Taxa Selic no BCB", e);
            return null;
        }
    }


}
