package com.falizsh.finance.shared.bcbifdata.web;

import com.falizsh.finance.shared.bcbifdata.adapter.BacenIfDataService;
import com.falizsh.finance.shared.bcbifdata.dto.BaselIndexResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/v1/finance/bacen/ifdata")
public class BacenIfDataController {

    private final BacenIfDataService bacenService;

    @GetMapping("/baselIndex")
    public ResponseEntity<BaselIndexResponse> getIndiceBasileia(
            @RequestParam String institution,
            @RequestParam String referenceDate
    ) {
        BaselIndexResponse response = bacenService.fetchBaselIndex(institution, referenceDate);
        return ResponseEntity.ok(response);
    }

}
