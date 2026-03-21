package com.falizsh.finance.marketdata.vna.repository.query;

import com.falizsh.finance.marketdata.vna.model.Vna;
import com.falizsh.finance.marketdata.vna.repository.VnaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@RequiredArgsConstructor
@Service
public class VnaQueryImpl implements VnaQuery {

    private final VnaRepository repository;

    @Override
    public List<Object[]> findIdentifiersByDateRange(LocalDate startDate, LocalDate endDate) {
        return repository.findIdentifiersByDateRange(startDate, endDate);
    }

    @Override
    public List<Vna> findByReferenceDate(LocalDate referenceDate) {
        return repository.findByReferenceDate(referenceDate);
    }
}
