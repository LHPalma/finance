package com.falizsh.finance.shared.web;

import com.falizsh.finance.shared.bcb.adapter.CdiRateLookupService;
import com.falizsh.finance.shared.bcb.adapter.dto.CdiRateInfoDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("v1/finance/utils/")
@RequiredArgsConstructor
public class EconomicsRateLookupController {

    private final CdiRateLookupService cdiService;


    @GetMapping("/di-rate/current")
    public ResponseEntity<CdiRateInfoDTO> getCurrentCdiRate() {
        CdiRateInfoDTO rate = cdiService.findLatestDailyRate();

        if (rate == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(rate);
    }
}