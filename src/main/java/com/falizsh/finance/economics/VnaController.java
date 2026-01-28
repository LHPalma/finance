package com.falizsh.finance.economics;

import com.falizsh.finance.economics.model.Vna;
import com.falizsh.finance.economics.service.VnaService;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.discoverer.SpringDocParameterNameDiscoverer;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/v1/finance/economics/vna")
@RequiredArgsConstructor
public class VnaController {

    private final VnaService vnaService;
    private final SpringDocParameterNameDiscoverer localSpringDocParameterNameDiscoverer;

    @PostMapping("/import")
    public ResponseEntity<List<Vna>> importVna(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date
    ) {
        LocalDate targetDate = (date != null) ? date : LocalDate.now();
        List<Vna> savedVnas = vnaService.fetchAndSaveVna(targetDate);
        return ResponseEntity.ok(savedVnas);
    }

    @PostMapping("/import/range")
    public ResponseEntity<List<Vna>> importRange(
            @RequestParam(required = true) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate
    ) {

        LocalDate today = LocalDate.now();
        LocalDate targetEndDate = (endDate != null) ? endDate : today;

        if (targetEndDate.isBefore(startDate)) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "A data final não pode ser anterior à data inícial."
            );
        }

        if (startDate.isAfter(today) || targetEndDate.isAfter(today)) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "As datas de importação não podem ser futuras. Hoje é " + today
            );
        }

        List<Vna> savedVnas = vnaService.fetchAndSaveVnaRange(startDate, targetEndDate);
        return ResponseEntity.ok(savedVnas);
    }

    @GetMapping
    public ResponseEntity<List<Vna>> fetchVna(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date
    ) {
        LocalDate targetDate = (date != null) ? date : LocalDate.now();
        return ResponseEntity.ok(vnaService.fetchFromAmbima(targetDate));
    }

}