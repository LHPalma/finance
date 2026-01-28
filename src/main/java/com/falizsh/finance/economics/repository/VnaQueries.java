package com.falizsh.finance.economics.repository;

public class VnaQueries {

    public static final String FIND_IDENTIFIERS_BY_DATE_RANGE_QUERY = """
                    SELECT
                        v.selicCode,
                        v.referenceDate
                    FROM vna As v
                    WHERE
                        v.referenceDate BETWEEN
                            :startDate AND :endDate;
            """;

}
