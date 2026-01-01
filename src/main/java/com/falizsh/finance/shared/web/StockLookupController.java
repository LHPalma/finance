    package com.falizsh.finance.shared.web;

    import com.falizsh.finance.shared.stock.adapter.StockLookupService;
    import com.falizsh.finance.shared.stock.adapter.dto.StockInfoDTO;
    import lombok.RequiredArgsConstructor;
    import lombok.extern.slf4j.Slf4j;
    import org.springframework.beans.factory.annotation.Qualifier;
    import org.springframework.http.ResponseEntity;
    import org.springframework.web.bind.annotation.GetMapping;
    import org.springframework.web.bind.annotation.PathVariable;
    import org.springframework.web.bind.annotation.RequestMapping;
    import org.springframework.web.bind.annotation.RestController;

    @Slf4j
    @RestController
    @RequestMapping("v1/finance/utils/stock")
    public class StockLookupController {

        private final StockLookupService orchestrator;
        private final StockLookupService yahooProvider;

        public StockLookupController(
                StockLookupService orchestrator,
                @Qualifier("yahooStockProvider") StockLookupService yahooProvider
                ) {
            this.orchestrator = orchestrator;
            this.yahooProvider = yahooProvider;
        }

        @GetMapping("/{ticker}")
        public ResponseEntity<StockInfoDTO> getStockPrice(@PathVariable String ticker) {
            return searchStock(orchestrator, ticker);
        }

        @GetMapping("/yahoo/{ticker}")
        public ResponseEntity<StockInfoDTO> getYahooDirect(@PathVariable String ticker) {
            log.warn("aqui");
            return searchStock(yahooProvider, ticker);
        }

        private ResponseEntity<StockInfoDTO> searchStock(StockLookupService service, String ticker) {
            StockInfoDTO stock = service.findStockPrice((ticker.toUpperCase()));

            if (stock == null){
                return ResponseEntity.notFound().build();
            }

            return ResponseEntity.ok(stock);
        }
    }