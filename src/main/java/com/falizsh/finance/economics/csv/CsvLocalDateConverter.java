package com.falizsh.finance.economics.csv;

import com.opencsv.bean.AbstractBeanField;
import com.opencsv.exceptions.CsvConstraintViolationException;
import com.opencsv.exceptions.CsvDataTypeMismatchException;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class CsvLocalDateConverter extends AbstractBeanField<String, LocalDate> {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    @Override
    protected Object convert(String value) throws CsvDataTypeMismatchException, CsvConstraintViolationException {

        try {
            return LocalDate.parse(value, FORMATTER);
        } catch (Exception e) {
            throw new CsvDataTypeMismatchException(value, field.getType(), "invalid.date.or.wrong.format.(dd/MM/yyyy)");
        }
    }
}
