package com.falizsh.finance.economics.adpater.dto;

import com.falizsh.finance.economics.model.Vna;
import com.falizsh.finance.economics.model.VnaStatus;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
@JacksonXmlRootElement(localName = "VNA")
public record AnbimaVnaResponse(
        @JacksonXmlProperty(localName = "Titulo")
        @JacksonXmlElementWrapper(useWrapping = false)
        List<AnbimaSecurity> securityList
) {

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record AnbimaSecurity(
            @JacksonXmlProperty(isAttribute = true, localName = "Papel")
            String security,

            @JacksonXmlProperty(localName = "Dados")
            AnbimaSecurityData data
    ) {

        public Vna toDomain(LocalDate fallbackDate) {
            return Vna.builder()
                    .security(this.security)
                    .selicCode(data.selicCode())
                    .referenceDate(Parsers.parseDate(data.referenceDate(), fallbackDate))
                    .price(Parsers.parseDecimal(data.price()))
                    .indexValue(Parsers.parseDecimal(data.indexValue()))
                    .status(VnaStatus.fromString(data.status()))
                    .build();
        }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record AnbimaSecurityData(
            @JacksonXmlProperty(isAttribute = true, localName = "Cod_Selic")
            String selicCode,
            @JacksonXmlProperty(isAttribute = true, localName = "DtRef_D0")
            String referenceDate,
            @JacksonXmlProperty(isAttribute = true, localName = "Valor_D0")
            String price,
            @JacksonXmlProperty(isAttribute = true, localName = "Index_D0")
            String indexValue,
            @JacksonXmlProperty(isAttribute = true, localName = "Ref_D0")
            String status
    ) {
    }

    private static class Parsers {
        static LocalDate parseDate(String dateStr, LocalDate fallback) {
            if (dateStr == null || dateStr.isBlank()) return fallback;
            try {
                return LocalDate.parse(dateStr);
            } catch (Exception e) {
                try {
                    return LocalDate.parse(dateStr, DateTimeFormatter.ofPattern("dd/MM/yyyy"));
                } catch (Exception e2) {
                    return fallback;
                }
            }
        }

        static BigDecimal parseDecimal(String value) {
            if (value == null || value.isBlank()) return null;
            return new BigDecimal(value.replace(".", "").replace(",", "."));
        }
    }
}