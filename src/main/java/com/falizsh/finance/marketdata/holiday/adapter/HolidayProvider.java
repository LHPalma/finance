package com.falizsh.finance.marketdata.holiday.adapter;

import com.falizsh.finance.marketdata.holiday.model.Holiday;
import com.falizsh.finance.marketdata.holiday.model.HolidayProviderResponse;

import java.util.List;

public interface HolidayProvider {

    HolidayProviderResponse fetchHolidays();

    String getProviderName();

}
