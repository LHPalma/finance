package com.falizsh.finance.shared.holiday.adapter;

import com.falizsh.finance.shared.holiday.model.Holiday;

import java.util.List;

public interface HolidayProvider {

    List<Holiday> fetchHolidays();

}
