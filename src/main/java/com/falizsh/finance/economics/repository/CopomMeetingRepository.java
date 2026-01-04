package com.falizsh.finance.economics.repository;

import com.falizsh.finance.economics.model.CopomMeeting;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.Optional;

import static com.falizsh.finance.economics.repository.CompomMeetingQueries.FIND_LAST_MEETING_QUERT;
import static com.falizsh.finance.economics.repository.CompomMeetingQueries.FIND_NEXT_MEETING_QUERY;

public interface CopomMeetingRepository extends JpaRepository<CopomMeeting, Long> {

    @Query(value = FIND_NEXT_MEETING_QUERY, nativeQuery = true)
    Optional<CopomMeeting> findNextMeeting(LocalDate date);

    @Query(value = FIND_LAST_MEETING_QUERT, nativeQuery = true)
    Optional<CopomMeeting> findLastCopomMeeting(LocalDate date);

}