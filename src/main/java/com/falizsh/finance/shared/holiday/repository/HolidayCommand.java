package com.falizsh.finance.shared.holiday.repository;

import com.falizsh.finance.shared.holiday.model.Holiday;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(rollbackFor = Exception.class)
public class HolidayCommand {

    private final HolidayRepository repository;

    public int saveAllIfNew(List<Holiday> holidays) {
        int savedCount = 0;

        for (Holiday holiday : holidays) {
            if (!repository.existsByDateAndCountryCode(holiday.getDate(), holiday.getCountryCode())) {
                repository.save(holiday);
                savedCount++;
            }
        }

        return savedCount;
    }

}
