package com.falizsh.finance.shared.web;

import com.falizsh.finance.economics.model.CopomMeeting;
import com.falizsh.finance.economics.repository.CopomMeetingRepository;
import com.falizsh.finance.shared.bcb.adapter.CdiRateLookupService;
import com.falizsh.finance.shared.bcb.adapter.SelicRateLookupService;
import com.falizsh.finance.shared.bcb.adapter.dto.CdiRateInfoDTO;
import com.falizsh.finance.shared.bcb.adapter.dto.SelicRateInfoDTO;
import com.falizsh.finance.shared.web.assembler.CdiAssembler;
import com.falizsh.finance.shared.web.assembler.CopomMeetingAssembler;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("v1/finance/utils/")
@RequiredArgsConstructor
public class EconomicsRateLookupController {

    private final CdiRateLookupService cdiService;
    private final SelicRateLookupService selicService;

    private final CdiAssembler cdiAssembler;

    @GetMapping("/di-rate/current")
    public ResponseEntity<CdiRateInfoDTO> getCurrentCdiRate() {
    public ResponseEntity<EntityModel<CdiRateInfoDTO>> getCurrentCdiRate() {
        CdiRateInfoDTO rate = cdiService.findLatestDailyRate();

        if (rate == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(rate);
        return ResponseEntity.ok(cdiAssembler.toModel(rate));
    }

    @GetMapping("selic-rate/current")
    @GetMapping("/selic-rate/current")
    public ResponseEntity<SelicRateInfoDTO> getCurrentSelicRate() {
        SelicRateInfoDTO rate = selicService.findLatestDailyRate();

        if (rate == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(rate);
    }
}