package com.falizsh.finance.economics;

import com.falizsh.finance.economics.model.Vna;
import com.falizsh.finance.economics.service.VnaService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/v1/finance/economics/vna")
@RequiredArgsConstructor
public class VnaController {

    private final VnaService vnaService;

    @PostMapping("/import")
    public ResponseEntity<List<Vna>> importVna(
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date
    ) {

        LocalDate targetDate = (date != null) ? date : LocalDate.now();
        List<Vna> savedVnas = vnaService.fetchAndSaveVna(targetDate);
        return ResponseEntity.ok(savedVnas);
    }

}