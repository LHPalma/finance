package com.falizsh.finance.shared.stock.adapter;

import com.falizsh.finance.shared.stock.adapter.dto.StockInfoDTO;

public interface StockLookupService {

    StockInfoDTO findStockPrice(String ticker);

}

