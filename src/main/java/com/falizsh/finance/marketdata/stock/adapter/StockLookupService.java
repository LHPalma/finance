package com.falizsh.finance.marketdata.stock.adapter;

import com.falizsh.finance.marketdata.stock.adapter.dto.StockInfoDTO;

public interface StockLookupService {

    StockInfoDTO findStockPrice(String ticker);

}

