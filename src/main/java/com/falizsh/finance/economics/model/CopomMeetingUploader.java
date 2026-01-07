package com.falizsh.finance.economics.model;

import com.falizsh.finance.economics.integration.CopomMeetingCsvUploadEvent;
import com.falizsh.finance.infra.file.FileStorageService;
import com.falizsh.finance.shared.validation.FileExtensions;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@RequiredArgsConstructor
@Validated
@Component
public class CopomMeetingUploader {

    private final FileStorageService fileStorageService;
    private final ApplicationEventPublisher eventPublisher;

    @Transactional
    public void upload(
            @FileExtensions(".csv") MultipartFile file
    ) {
        String path = fileStorageService.storeFile(file);

        eventPublisher.publishEvent(new CopomMeetingCsvUploadEvent(path));
    }

}
