package com.falizsh.finance.economics.adpater.impl;

import com.falizsh.finance.economics.adpater.AnbimaClient;
import com.falizsh.finance.economics.adpater.dto.AnbimaVnaResponse;
import com.falizsh.finance.economics.adpater.dto.VnaResult;
import com.falizsh.finance.economics.model.Vna;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import feign.Response;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Slf4j
@Component
@RequiredArgsConstructor
public class AnbimaProvider {

    private final AnbimaClient client;

    private final XmlMapper xmlMapper = new XmlMapper();

    public VnaResult fetchVna(LocalDate date) {

        byte[] rawContent;

        try {
            rawContent = dowloadXml(date);
        } catch (IOException e) {
            log.error("Erro no download da Anbima", e);
            return VnaResult.failure(null, "Download failed: " + e.getMessage());
        }

        if (!isValidXml(rawContent)) {
            return VnaResult.failure(rawContent, "Invalid XML format");
        }

        try {

            List<Vna> vnas = parse(rawContent, date);
            if (vnas.isEmpty()) {
                return VnaResult.failure(null, "XML válido mas sem registros (lista vazia)");
            }

            return VnaResult.success(vnas, rawContent);

        } catch (Exception e) {
            log.error("Falha ao parsear XML da Anbima", e);
            return VnaResult.failure(rawContent, "Parse error: " + e.getMessage());
        }

    }

    private byte[] dowloadXml(LocalDate date) throws IOException {
        String dataParam = date.format(DateTimeFormatter.ofPattern("ddMMyyyy"));
        String requestBody = "saida=xml&Idioma=PT&Data=" + dataParam;

        Response response = client.downloadVnaXml(requestBody);
        try (var inputStream = response.body().asInputStream()) {
            return inputStream.readAllBytes();
        }
    }

    private List<Vna> parse(byte[] content, LocalDate referenceDate) throws IOException {
        String xmlBody = new String(content, StandardCharsets.ISO_8859_1);
        AnbimaVnaResponse response = xmlMapper.readValue(xmlBody, AnbimaVnaResponse.class);

        if (response.securityList() == null) {
            return Collections.emptyList();
        }

        return mapToDomain(response.securityList(), referenceDate);
    }

    private boolean isValidXml(byte[] content) {
        if (content == null || content.length == 0) return false;
        String sample = new String(content, 0, Math.min(content.length, 50), StandardCharsets.ISO_8859_1).trim();
        return sample.startsWith("<") && !sample.startsWith("<!DOCTYPE html");
    }

    private List<Vna> mapToDomain(List<AnbimaVnaResponse.AnbimaSecurity> dtos, LocalDate referenceDate) {
        return dtos.stream()
                .map(dto -> {
                    try {
                        return dto.toDomain(referenceDate);
                    } catch (Exception e) {
                        log.warn("Ignorando item inválido no XML ({}): {}", dto.security(), e.getMessage());
                        return null; // Será filtrado abaixo
                    }
                })
                .filter(Objects::nonNull)
                .toList();
    }

}