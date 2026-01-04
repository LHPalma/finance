package com.falizsh.finance.economics.csv;

import com.opencsv.bean.AbstractBeanField;
import com.opencsv.exceptions.CsvDataTypeMismatchException;
import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.ParseException;
import java.util.Locale;

public class CsvBigDecimalConverter extends AbstractBeanField<String, BigDecimal> {

    @Override
    protected Object convert(String value) throws CsvDataTypeMismatchException {

        if (StringUtils.isBlank(value)) {
            return null;
        }

        try {

            DecimalFormatSymbols symbols = new DecimalFormatSymbols(new Locale("pt", "BR"));
            symbols.setGroupingSeparator('.');
            symbols.setMonetaryDecimalSeparator(',');

            DecimalFormat decimalFormat = new DecimalFormat("#,##0.0#", symbols);
            decimalFormat.setParseBigDecimal(true);

            return (BigDecimal) decimalFormat.parse(value);


        } catch (ParseException e) {
            throw new CsvDataTypeMismatchException(value, BigDecimal.class, "failed.to.convert.monetary.value." + value);
        }

    }

}
