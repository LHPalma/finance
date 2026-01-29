package com.falizsh.finance.shared.bcbifdata.web;

import com.falizsh.finance.shared.bcbifdata.adapter.BacenSrapperService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/scraper/bacen")
public class BacenScrapperController {

    private final BacenSrapperService scraperService;

    @PostMapping("/resumo")
    public ResponseEntity<String> scrapeResumo(
            @RequestParam String dataBase
    ) {
        String path = scraperService.scrapeResumo(dataBase);
        return ResponseEntity.ok("Arquivo importado com sucesso. Local: " + path);
    }
}
