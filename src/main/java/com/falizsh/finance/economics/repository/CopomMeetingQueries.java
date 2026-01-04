package com.falizsh.finance.economics.repository;

public class CopomMeetingQueries {

    public static final String FIND_NEXT_MEETING_QUERY = """
            SELECT
                id,
                meeting_date,
                meeting_number,
                description,
                previous_selic_target,
                selic_target,
                minutes_url,
                created_at,
                updated_at
            FROM
                copom_meeting
            WHERE
                meeting_date >= :date
            ORDER BY
                meeting_date
                ASC
            LIMIT
                1;
            
            """;

    public static final String FIND_LAST_MEETING_QUERY = """
            SELECT
                id,
                meeting_date,
                meeting_number,
                description,
                previous_selic_target,
                selic_target,
                minutes_url,
                created_at,
                updated_at
            FROM copom_meeting
            WHERE
                meeting_date < :date
            ORDER BY
                meeting_date
                DESC
            LIMIT
                1;
            """;
}
