package com.falizsh.finance.integrations.stock.adapter;

import com.falizsh.finance.integrations.stock.adapter.dto.StockInfoDTO;

public interface StockLookupService {

    StockInfoDTO findStockPrice(String ticker);

}

