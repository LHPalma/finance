package com.falizsh.finance.infrastructure.audit.converter;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import org.postgresql.util.PGobject;

import java.sql.SQLException;

@Converter
public class InetConverter implements AttributeConverter<String, Object> {

    @Override
    public Object convertToDatabaseColumn(String ip) {
        if (ip == null || ip.isBlank()) {
            return null;
        }

        try {
            PGobject pgObject = new PGobject();
            pgObject.setType("inet");
            pgObject.setValue(ip);
            return pgObject;
        } catch (SQLException e) {
            throw new IllegalArgumentException("invalid.ip.address:" + ip, e);
        }
    }

    @Override
    public String convertToEntityAttribute(Object dbData) {
        if (dbData == null) {
            return null;
        }
        return dbData.toString();
    }
}