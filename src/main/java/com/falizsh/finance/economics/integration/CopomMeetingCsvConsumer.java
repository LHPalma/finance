package com.falizsh.finance.economics.integration;

import com.falizsh.finance.economics.repository.CopomMeetingCsvReader;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Component
@RequiredArgsConstructor
public class CopomMeetingCsvConsumer {

    private final CopomMeetingCsvReader reader;

    @Async
    @EventListener
    @Transactional
    public void onReceiveMessage(CopomMeetingCsvUploadEvent event) {
        reader.process(event.getFilePath());
    }
}
