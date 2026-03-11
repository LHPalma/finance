package com.falizsh.finance.marketdata.copomMeeting.repository;


import com.falizsh.finance.marketdata.copomMeeting.csv.CopomMeetingCsv;
import com.falizsh.finance.marketdata.copomMeeting.model.CopomMeeting;
import com.falizsh.finance.infrastructure.file.FileStorageService;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class CopomMeetingCsvReader {

    private final FileStorageService fileStorageService;
    private final CopomMeetingCommand command;

    @Transactional
    public void process(String filePath) {

        Path path = fileStorageService.loadFile(filePath);

        try (Reader reader = Files.newBufferedReader(path, StandardCharsets.ISO_8859_1)) {

            CsvToBean<CopomMeetingCsv> csvToBean = new CsvToBeanBuilder<CopomMeetingCsv>(reader)
                    .withType(CopomMeetingCsv.class)
                    .withIgnoreLeadingWhiteSpace(true)
                    .withSeparator(';')
                    .build();

            List<CopomMeetingCsv> lines = csvToBean.parse();

            lines.forEach(dto -> {
                CopomMeeting entity = CopomMeeting.builder()
                        .meetingDate(dto.getMeetingDate())
                        .meetingNumber(dto.getMeetingNumber())
                        .description(dto.getDescription())
                        .previousSelicTarget(dto.getPreviousSelicTarget())
                        .selicTarget(dto.getSelicTarget())
                        .minutesUrl(dto.getMinutesUrl())
                        .build();

                command.save(entity);
            });

            Files.deleteIfExists(path);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

}
