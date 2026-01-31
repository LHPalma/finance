package com.falizsh.finance.shared.holiday.web;

import com.falizsh.finance.shared.holiday.model.CountryCode;
import com.falizsh.finance.shared.holiday.model.Holiday;
import com.falizsh.finance.shared.holiday.repository.HolidayQuery;
import com.falizsh.finance.shared.holiday.service.HolidayImportService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/finance/v1/routines/holidays")
@RequiredArgsConstructor
public class HolidayController {

    private final HolidayImportService holidayImportService;
    private final HolidayQuery holidayQuery;

    @PostMapping("/sync")
    public ResponseEntity<String> triggerManualImport() {
        holidayImportService.executeWeeklyImport();
        return ResponseEntity.ok("Sincronização de feriados finalizada com sucesso!");
    }

    @GetMapping
    public ResponseEntity<List<Holiday>> getHolidays(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @RequestParam(defaultValue = "BR") CountryCode countryCode
    ) {
        LocalDate targetStartDate = startDate != null ? startDate : LocalDate.now().withDayOfYear(1);
        LocalDate targetEndDate = endDate  != null ? endDate : LocalDate.now().withDayOfYear(LocalDate.now().lengthOfYear());

        return ResponseEntity.ok(holidayQuery.findByRange(targetStartDate, targetEndDate, countryCode));
    }
}