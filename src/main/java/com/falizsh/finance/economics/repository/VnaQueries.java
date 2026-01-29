package com.falizsh.finance.economics.repository;

public class VnaQueries {

    public static final String FIND_IDENTIFIERS_BY_DATE_RANGE_QUERY = """
                    SELECT
                        v.selic_code,
                        v.reference_date
                    FROM vna As v
                    WHERE
                        v.reference_date BETWEEN
                            :startDate AND :endDate;
            """;

}
