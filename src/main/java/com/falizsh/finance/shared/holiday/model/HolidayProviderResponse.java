package com.falizsh.finance.shared.holiday.model;

import java.util.List;


public record HolidayProviderResponse(
        byte[] rawContent,
        String fileName,
        String contentType,
        List<Holiday> holidays
) {
}

