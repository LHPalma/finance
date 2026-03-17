package com.falizsh.finance.marketdata.vna.web;

import com.falizsh.finance.marketdata.vna.model.Vna;
import com.falizsh.finance.marketdata.vna.usecase.FetchVnaDataUseCase;
import com.falizsh.finance.marketdata.vna.usecase.ImportVnaDataRangeUseCase;
import com.falizsh.finance.marketdata.vna.usecase.ImportVnaDataUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/economics/vna")
@RequiredArgsConstructor
public class VnaController {

    private final ImportVnaDataUseCase importVnaDataUseCase;
    private final ImportVnaDataRangeUseCase importVnaDataRangeUseCase;
    private final FetchVnaDataUseCase fetchVnaDataUseCase;

    @PostMapping("/import")
    public ResponseEntity<List<Vna>> importVna(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date
    ) {
        LocalDate targetDate = (date != null) ? date : LocalDate.now();

        List<Vna> savedVnas = importVnaDataUseCase.execute(targetDate);

        return ResponseEntity.ok(savedVnas);
    }

    @PostMapping("/import/range")
    public ResponseEntity<List<Vna>> importRange(
            @RequestParam() @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate
    ) {

        LocalDate today = LocalDate.now();
        LocalDate targetEndDate = (endDate != null) ? endDate : today;

        if (targetEndDate.isBefore(startDate)) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "A data final não pode ser anterior à data inicial."
            );
        }

        if (startDate.isAfter(today) || targetEndDate.isAfter(today)) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "As datas de importação não podem ser futuras. Hoje é " + today
            );
        }

        List<Vna> savedVnas = importVnaDataRangeUseCase.execute(startDate, targetEndDate);

        return ResponseEntity.ok(savedVnas);
    }

    @GetMapping
    public ResponseEntity<List<Vna>> fetchVna(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date
    ) {
        LocalDate targetDate = (date != null) ? date : LocalDate.now();

        return ResponseEntity.ok(fetchVnaDataUseCase.execute(targetDate));
    }
}