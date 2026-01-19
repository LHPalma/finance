package com.falizsh.finance.shared.holiday.adapter.impl;

import com.falizsh.finance.shared.holiday.adapter.CloudConvertClient;
import com.falizsh.finance.shared.holiday.adapter.HolidayProvider;
import com.falizsh.finance.shared.holiday.adapter.dto.CloudConvertJobRequest;
import com.falizsh.finance.shared.holiday.model.CountryCode;
import com.falizsh.finance.shared.holiday.model.Holiday;
import com.falizsh.finance.shared.holiday.model.HolidayProviderResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class AnbimaHolidayProvider implements HolidayProvider {

    private final CloudConvertClient cloudConvertClient;

    @Value("${cloudconvert.token}")
    private String token;

    @Value("${anbima.url}")
    private String anbimaUrl;

    private static final LocalDate EXCEL_EPOCH = LocalDate.of(1899, 12, 30);

    @Override
    public HolidayProviderResponse fetchHolidays() {
        try {
            log.info("Iniciando busca de feriados na ANBIMA via CloudConvert...");

            String bearerToken = "Bearer " + token;
            var request = CloudConvertJobRequest.createForAnbima(anbimaUrl);
            var response = cloudConvertClient.createJob(bearerToken, request);
            String jobId = response.data().id();
            log.info("Job criado: {}", jobId);

            String downloadUrl = waitForCompletion(jobId, bearerToken);
            log.info("Conversão concluída. Baixando arquivo...");

            byte[] csvBytes = cloudConvertClient.downloadFile(URI.create(downloadUrl));

            String csvContent = new String(csvBytes, StandardCharsets.ISO_8859_1);

            List<Holiday> holidays = parseCsv(csvContent);

            return new HolidayProviderResponse(
                    csvBytes,
                    "anbima_holidays_raw.csv",
                    "text/csv",
                    holidays
            );

        } catch (Exception e) {
            log.error("Falha na integração de feriados", e);
            return new HolidayProviderResponse(new byte[0], "", "", Collections.emptyList());
        }
    }

    @Override
    public String getProviderName() {
        return "Anbima";
    }

    private String waitForCompletion(String jobId, String bearerToken) throws InterruptedException {
        int timeoutInSeconds = 60;
        int waitTimeBetweenRetriesInMillis = 2000;

        for (int i = 0; i < timeoutInSeconds / 2; i++) {
            var response = cloudConvertClient.getJob(bearerToken, jobId);
            String status = response.data().status();

            if ("finished".equals(status)) {
                return response.findExportUrl();
            } else if ("error".equals(status)) {
                throw new RuntimeException("O Job falhou no CloudConvert.");
            }
            Thread.sleep(waitTimeBetweenRetriesInMillis);
        }
        throw new RuntimeException("Timeout esperando a conversão do arquivo.");
    }

    private List<Holiday> parseCsv(String csvContent) {
        List<Holiday> holidays = new ArrayList<>();
        List<String> lines = csvContent.lines().toList();

        log.info("Processando {} linhas do CSV...", lines.size());

        for (String rawLine : lines) {
            String line = rawLine.trim();
            if (line.matches("^\\d+.*")) {
                try {
                    String separator = line.contains(";") ? ";" : ",";
                    String[] cols = line.split(separator);

                    if (cols.length >= 3) {
                        String dateStr = cols[0].replace("\"", "").trim();
                        String name = cols[2].replace("\"", "").trim();

                        LocalDate date = parseDateRobust(dateStr);

                        holidays.add(Holiday.builder()
                                .date(date)
                                .name(name)
                                .countryCode(CountryCode.BR)
                                .build());
                    }
                } catch (Exception e) {
                    log.warn("Linha ignorada: '{}'. Motivo: {}", line, e.getMessage());
                }
            }
        }

        log.info("Total de feriados processados com sucesso: {}", holidays.size());
        return holidays;
    }

    private LocalDate parseDateRobust(String dateStr) {
        try {
            double serial = Double.parseDouble(dateStr);
            return EXCEL_EPOCH.plusDays((long) serial);
        } catch (NumberFormatException e) {
            try {
                return LocalDate.parse(dateStr, DateTimeFormatter.ofPattern("M/d/yyyy"));
            } catch (Exception ex) {
                return LocalDate.parse(dateStr, DateTimeFormatter.ofPattern("dd/MM/yyyy"));
            }
        }
    }
}