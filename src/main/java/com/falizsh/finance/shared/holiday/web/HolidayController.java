package com.falizsh.finance.shared.holiday.web;

import com.falizsh.finance.shared.holiday.service.HolidayImportService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/finance/v1/routines/holidays")
@RequiredArgsConstructor
public class HolidayController {

    private final HolidayImportService holidayImportService;

    @PostMapping("/sync")
    public ResponseEntity<String> triggerManualImport() {
        holidayImportService.executeWeeklyImport();

        return ResponseEntity.ok("Sincronização de feriados finalizada com sucesso!");
    }
}