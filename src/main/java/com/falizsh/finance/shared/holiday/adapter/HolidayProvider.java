package com.falizsh.finance.shared.holiday.adapter;

import com.falizsh.finance.shared.holiday.model.Holiday;
import com.falizsh.finance.shared.holiday.model.HolidayProviderResponse;

import java.util.List;

public interface HolidayProvider {

    HolidayProviderResponse fetchHolidays();

    String getProviderName();

}
