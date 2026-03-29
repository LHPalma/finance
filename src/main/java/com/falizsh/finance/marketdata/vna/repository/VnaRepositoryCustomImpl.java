package com.falizsh.finance.marketdata.vna.repository;

import com.falizsh.finance.marketdata.vna.model.QVna;
import com.falizsh.finance.marketdata.vna.repository.projections.VnaIdentifierData;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@RequiredArgsConstructor
@Repository
public class VnaRepositoryCustomImpl implements VnaRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<VnaIdentifierData> findIdentifiersByDateRange(LocalDate startDate, LocalDate endDate) {
        QVna vna = QVna.vna;

        return queryFactory
                .select(Projections.constructor(
                        VnaIdentifierData.class,
                        vna.selicCode,
                        vna.referenceDate
                ))
                .from(vna)
                .where(vna.referenceDate.between(startDate, endDate))
                .fetch();
    }

}
