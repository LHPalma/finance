package com.falizsh.finance.apis.b3.adapter;

import com.falizsh.finance.apis.b3.adapter.dto.B3DiResponse;
import com.falizsh.finance.apis.b3.application.dto.DiFutureResult;
import com.falizsh.finance.apis.b3.application.port.DiFutureGateway;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

@Slf4j
@Service
@RequiredArgsConstructor
public class B3DiProvider implements DiFutureGateway {

    private final B3DiClient client;

    @Override
    @Cacheable(value = "di_curve", key = "'full'")
    public List<DiFutureResult> getSettlementPrices() {
        log.info("Buscando dados reais na B3 [SEM CACHE]");

        try {
            B3DiResponse response = client.getDerivativeQuotation("DI1");

            if  (response == null || response.securities() == null) {
                return Collections.emptyList();
            }

            return response.securities().stream()
                    .filter(s -> s.market() != null && "FUT".equalsIgnoreCase(s.market().code()))
                    .filter(s -> s.quotation() != null && s.quotation().currentPrice() != null)
                    .sorted(Comparator.comparing(s -> s.asset().summary().maturityDate()))
                    .map(this::mapToDomain)
                    .filter(Objects::nonNull)
                    .toList();

        } catch (Exception e) {
            log.error("Erro ao buscar dados do DI1 na B3", e);
            return Collections.emptyList();
        }
    }

    private DiFutureResult mapToDomain(B3DiResponse.B3Security security) {
        try {
            Double price = security.quotation().currentPrice();
            // Fallback: Se não tem preço atual, usa o ajuste do dia anterior
            if (price == null) {
                price = security.quotation().previousAdjustmentPrice();
            }

            if (price == null) return null;

            return new DiFutureResult(
                    security.symbol(),
                    LocalDate.parse(security.asset().summary().maturityDate()),
                    BigDecimal.valueOf(price),
                    BigDecimal.valueOf(security.quotation().previousAdjustmentPrice() != null ? security.quotation().previousAdjustmentPrice() : 0.0)
            );
        } catch (Exception e) {
            log.warn("Erro ao converter contrato B3: {}", security.symbol());
            return null;
        }
    }

}
