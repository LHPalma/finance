package com.falizsh.finance.analytics.web;

import com.falizsh.finance.analytics.PricingService;
import com.falizsh.finance.analytics.dto.SimulationRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
@RestController
@RequestMapping("/simulation")
public class SimulationController {

    private final PricingService pricingService;

    @PostMapping("/ntnb")
    public ResponseEntity<Map<String, Object>> simulateNtnb(
            @RequestBody SimulationRequest request
    ) {

        int businessDays = pricingService.countBusinessDays(request.calculationDate(), request.maturityDate());

        BigDecimal unitPrice = pricingService.calculateNtnbPrice(
                request.vna(),
                request.annualRate(),
                request.projectedInflation(),
                request.calculationDate(),
                request.maturityDate()
        );

        Map<String, Object> response = new HashMap<>();
        response.put("dataBase", request.calculationDate());
        response.put("vencimento", request.maturityDate());
        response.put("diasUteis", businessDays);
        response.put("precoUnitario", unitPrice);
        response.put("vnaUtilizado", request.vna());
        response.put("taxaAnual", request.annualRate().multiply(new BigDecimal("100")) + "%");

        return ResponseEntity.ok(response);

    }


}
