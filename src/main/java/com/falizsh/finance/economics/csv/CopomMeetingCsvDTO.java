package com.falizsh.finance.economics.csv;

import com.opencsv.bean.CsvBindByName;
import com.opencsv.bean.CsvCustomBindByName;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class CopomMeetingCsvDTO {

    @CsvCustomBindByName(column = "DATA", required = true, converter = CsvLocalDateConverter.class)
    private LocalDate meetingDate;

    @CsvBindByName(column = "NÚMERO", required = true)
    private Integer meetingNumber;

    @CsvBindByName(column = "DESCRIÇÃO")
    private String description;

    @CsvCustomBindByName(column = "META_ANTERIOR", converter = CsvBigDecimalConverter.class)
    private BigDecimal previousSelicTarget;

    @CsvCustomBindByName(column = "META_NOVA", converter = CsvBigDecimalConverter.class)
    private BigDecimal selicTarget;

    @CsvBindByName(column = "URL_ATA")
    private String minutesUrl;

}
