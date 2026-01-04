package com.falizsh.finance.shared.web;

import com.falizsh.finance.economics.model.CopomMeeting;
import com.falizsh.finance.economics.model.CopomMeetingUploader;
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
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;

@RestController
@RequestMapping("v1/finance/utils/")
@RequiredArgsConstructor
public class EconomicsRateLookupController {

    private final CdiRateLookupService cdiService;
    private final SelicRateLookupService selicService;

    private final CdiAssembler cdiAssembler;

    private final CopomMeetingAssembler copomMeetingAssembler;
    private final CopomMeetingRepository copomMeetingRepository;
    private final CopomMeetingUploader copomMeetingUploader;

    @GetMapping("/di-rate/current")
    public ResponseEntity<EntityModel<CdiRateInfoDTO>> getCurrentCdiRate() {
        CdiRateInfoDTO rate = cdiService.findLatestDailyRate();

        if (rate == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(cdiAssembler.toModel(rate));
    }

    @GetMapping("/selic-rate/current")
    public ResponseEntity<SelicRateInfoDTO> getCurrentSelicRate() {
        SelicRateInfoDTO rate = selicService.findLatestDailyRate();

        if (rate == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(rate);
    }

    @GetMapping("/copom/next-meeting")
    public ResponseEntity<EntityModel<CopomMeeting>> getNextCopomMeeting() {

        return ResponseEntity.ok(copomMeetingAssembler.toModel(copomMeetingRepository.findNextMeeting(LocalDate.now()).orElseThrow()));
    }

    @PostMapping("/copom/import")
    public ResponseEntity<Void> uploadCopomMeetings(
            @RequestParam("file") MultipartFile file
    ) {
        copomMeetingUploader.upload(file);

        return ResponseEntity.accepted().build();
    }
}