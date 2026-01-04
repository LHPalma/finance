package com.falizsh.finance.economics.model;

import com.falizsh.finance.economics.integration.CopomMeetingCsvUploadEvent;
import com.falizsh.finance.infra.file.FileStorageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@Component
@RequiredArgsConstructor
public class CopomMeetingUploader {

    private final FileStorageService fileStorageService;
    private final ApplicationEventPublisher eventPublisher;

    @Transactional
    public void upload(MultipartFile file) {
        String path = fileStorageService.storeFile(file);

        eventPublisher.publishEvent(new CopomMeetingCsvUploadEvent(path));
    }

}
