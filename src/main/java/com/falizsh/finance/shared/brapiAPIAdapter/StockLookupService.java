package com.falizsh.finance.shared.brapiAPIAdapter;

import com.falizsh.finance.shared.brapiAPIAdapter.dto.StockInfoDTO;

public interface StockLookupService {

    StockInfoDTO findStockPrice(String ticker);

}

