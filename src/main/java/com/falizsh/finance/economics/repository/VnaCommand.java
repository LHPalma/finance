package com.falizsh.finance.economics.repository;

import com.falizsh.finance.economics.model.Vna;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class VnaCommand {

    private final VnaRepository repository;

    @Transactional
    public List<Vna> saveAllIgnoringDuplicates(List<Vna> vnas) {
        if (vnas == null || vnas.isEmpty()) {
            return Collections.emptyList();
        }

        LocalDate minDate = vnas.stream().map(Vna::getReferenceDate).min(LocalDate::compareTo).orElse(LocalDate.now());
        LocalDate maxDate = vnas.stream().map(Vna::getReferenceDate).max(LocalDate::compareTo).orElse(LocalDate.now());

        List<Object[]> existingRecords = repository.findIdentifiersByDateRange(minDate, maxDate);

        Set<String> existingKeys = existingRecords.stream()
                .map(row -> row[0] + "|" + row[1].toString())
                .collect(Collectors.toSet());

        List<Vna> toSave = vnas.stream()
                .filter(vna -> {
                    String key = vna.getSelicCode() + "|" + vna.getReferenceDate().toString();
                    return !existingKeys.contains(key);
                })
                .toList();

        if (toSave.isEmpty()) {
            return Collections.emptyList();
        }

        return repository.saveAll(vnas);
    }

}
