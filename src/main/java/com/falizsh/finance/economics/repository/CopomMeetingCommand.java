package com.falizsh.finance.economics.repository;

import com.falizsh.finance.economics.model.CopomMeeting;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(rollbackFor = Exception.class)
public class CopomMeetingCommand {

    private final CopomMeetingRepository repository;

    public CopomMeeting save(CopomMeeting copomMeeting) {
        return repository.save(copomMeeting);
    }

}
