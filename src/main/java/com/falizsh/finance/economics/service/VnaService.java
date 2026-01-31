package com.falizsh.finance.economics.service;


import com.falizsh.finance.economics.adpater.AnbimaClient;
import com.falizsh.finance.economics.adpater.dto.AnbimaVnaResponse;
import com.falizsh.finance.economics.model.Vna;
import com.falizsh.finance.economics.model.VnaStatus;
import com.falizsh.finance.economics.repository.VnaRepository;
import com.falizsh.finance.infra.sourcefile.model.SourceFile;
import com.falizsh.finance.infra.sourcefile.model.SourceFileDomain;
import com.falizsh.finance.infra.sourcefile.model.SourceFileStatus;
import com.falizsh.finance.infra.sourcefile.repository.SourceFileCommand;
import com.falizsh.finance.infra.storage.FileStorageProvider;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import feign.Response;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class VnaService {

    private final AnbimaClient anbimaClient;
    private final VnaRepository vnaRepository;
    private final XmlMapper xmlMapper = new XmlMapper();
    private final SourceFileCommand sourceFileCommand;


    public List<Vna> fetchFromAmbima(LocalDate referenceDate) {
        String dataParam = referenceDate.format(DateTimeFormatter.ofPattern("ddMMyyyy"));
        String requestBody = "saida=xml&Idioma=PT&Data=" + dataParam;

        SourceFile sourceFile = null;

        try {
            log.info("Iniciando consulta do VNA para data: {}", dataParam);

            Response response = anbimaClient.downloadVnaXml(requestBody);

            byte[] content = response.body().asInputStream().readAllBytes();
            try {
                String fileName = "vna_" + referenceDate.toString() + ".xml";
                sourceFile = sourceFileCommand.create(
                        fileName,
                        content,
                        "application/xml",
                        SourceFileDomain.VNA,
                        null,
                        null
                );

            } catch (Exception e) {
                log.error("Erro ao salvar arquivo XML do VNA no storage: {}", e.getMessage());
                return List.of();
            }

            String responseBody = new String(content, StandardCharsets.ISO_8859_1);

            String trimmedBody = responseBody.trim();
            if (!trimmedBody.startsWith("<") || trimmedBody.startsWith("<!DOCTYPE html")) {
                String errorMsg = "Conteúdo não é XML: " + responseBody.substring(0, Math.min(50, responseBody.length()));
                sourceFileCommand.addError(sourceFile, errorMsg, trimmedBody);
                sourceFileCommand.updateStatus(sourceFile, SourceFileStatus.FAILED);
                log.warn(errorMsg);
                return List.of();
            }

            AnbimaVnaResponse vnaResponse = xmlMapper.readValue(responseBody, AnbimaVnaResponse.class);
            if (vnaResponse.securityList() == null || vnaResponse.securityList().isEmpty()) {
                sourceFileCommand.updateStatus(sourceFile, SourceFileStatus.PROCESSED);
                return List.of();
            }

            List<Vna> parsedVna = new ArrayList<>();
            boolean hasErrors = false;

            for (AnbimaVnaResponse.AnbimaSecurity security : vnaResponse.securityList()) {

                try {
                    var data = security.data();

                    LocalDate vnaDate = parseAnbimaDate(data.referenceDate(), referenceDate);

                    Vna vna = Vna.builder()
                            .security(security.security())
                            .selicCode(data.selicCode())
                            .referenceDate(vnaDate)
                            .price(parseAnbimaDecimal(data.price()))
                            .indexValue(parseAnbimaDecimal(data.indexValue()))
                            .status(VnaStatus.fromString(data.status()))
                            .build();

                    parsedVna.add(vna);

                } catch (Exception e) {
                    hasErrors = true;
                    String errorMessage = "Erro no título " + security.security() + ": " + e.getMessage();
                    log.warn(errorMessage);
                    sourceFileCommand.addError(sourceFile, errorMessage, security.toString());
                }
            }

            if (hasErrors) {
                sourceFileCommand.updateStatus(sourceFile, parsedVna.isEmpty() ? SourceFileStatus.FAILED : SourceFileStatus.PARTIAL_FAILURE);
            } else {
                sourceFileCommand.updateStatus(sourceFile, SourceFileStatus.PROCESSED);
            }

            return parsedVna;

        } catch (IOException e) {
            log.error("Erro de I/O ao processar resposta da Anbima", e);

            if (sourceFile != null) {
                try {
                    sourceFileCommand.addError(sourceFile, "Falha fatal de I/O: " + e.getMessage(), null);
                    sourceFileCommand.updateStatus(sourceFile, SourceFileStatus.FAILED);
                } catch (Exception dbEx) {
                    log.error("Erro ao atualizar status de falha no banco", dbEx);
                }
            }
            throw new RuntimeException("Falha ao integrar com Anbima: " + e.getMessage(), e);
        }
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public List<Vna> fetchAndSaveVna(LocalDate referenceDate) {
        return fetchAndSaveVnaRange(referenceDate, referenceDate);
    }

    private LocalDate parseAnbimaDate(String dateStr, LocalDate fallback) {
        if (dateStr == null || dateStr.isBlank()) {
            return fallback;
        }

        try {
            return LocalDate.parse(dateStr);
        } catch (Exception e1) {
            try {
                return LocalDate.parse(dateStr, DateTimeFormatter.ofPattern("dd/MM/yyyy"));
            } catch (Exception e2) {
                log.warn("Não foi possível parsear a data '{}'. Usando data de referência.", dateStr);
                return fallback;
            }
        }
    }

    private BigDecimal parseAnbimaDecimal(String value) {
        if (value == null || value.isBlank()) {
            return null;
        }
        String normalized = value.replace(".", "").replace(",", ".");
        return new BigDecimal(normalized);
    }

    @Transactional
    public List<Vna> fetchAndSaveVnaRange(LocalDate startDate, LocalDate endDate) {

        List<Vna> fetchedVna = new ArrayList<>();

        startDate.datesUntil(endDate.plusDays(1)).forEach(date -> {
            try {
                List<Vna> dayVnas = fetchFromAmbima(date);
                fetchedVna.addAll(dayVnas);
            } catch (Exception e) {
                log.error("Erro ao importar VNA para a data {}: {}", date, e.getMessage());
            }
        });

        List<Object[]> existingRecords = vnaRepository.findIdentifiersByDateRange(startDate, endDate);
        Set<String> existingKeys = existingRecords.stream()
                .map(row -> row[0] + "|" + row[1].toString())
                .collect(Collectors.toSet());

        List<Vna> vnasToSave = fetchedVna.stream()
                .filter(vna -> {
                    String key = vna.getSelicCode() + "|" + vna.getReferenceDate().toString();
                    return !existingKeys.contains(key);
                })
                .toList();

        return vnaRepository.saveAll(vnasToSave);
    }
}
