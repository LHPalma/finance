package com.falizsh.finance.marketdata.holiday.web;

import com.falizsh.finance.marketdata.holiday.model.CountryCode;
import com.falizsh.finance.marketdata.holiday.projection.HolidayProjection;
import com.falizsh.finance.marketdata.holiday.projection.HolidayProjectionModel;
import com.falizsh.finance.marketdata.holiday.repository.HolidayQuery;
import com.falizsh.finance.marketdata.holiday.service.HolidayImportService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/routines/holidays")
@RequiredArgsConstructor
public class HolidayController {

    private final HolidayImportService holidayImportService;
    private final HolidayQuery holidayQuery;
    private final HolidayAssembler assembler;

    @PostMapping("/sync")
    public ResponseEntity<String> triggerManualImport() {
        holidayImportService.executeWeeklyImport();
        return ResponseEntity.ok("Sincronização de feriados finalizada com sucesso!");
    }

    @GetMapping
    public ResponseEntity<CollectionModel<EntityModel<HolidayProjectionModel>>> getHolidays(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @RequestParam(defaultValue = "BR") CountryCode countryCode,
            Pageable pageable,
            PagedResourcesAssembler<HolidayProjection> pagedAssembler
    ) {
        LocalDate targetStartDate = startDate != null ? startDate : LocalDate.now().withDayOfYear(1);
        LocalDate targetEndDate = endDate != null ? endDate : LocalDate.now().withDayOfYear(LocalDate.now().lengthOfYear());

        return ResponseEntity.ok(
                pagedAssembler.toModel(
                        holidayQuery.findByRangePaginated(targetStartDate, targetEndDate, countryCode, pageable),
                        assembler
                )
        );
    }
}