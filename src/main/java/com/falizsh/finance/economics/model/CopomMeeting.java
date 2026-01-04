package com.falizsh.finance.economics.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "CopomMeeting")
@Table(name = "copom_meeting")
public class CopomMeeting {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Long id;

    @Column(nullable = false)
    private LocalDate meetingDate;

    private Integer meetingNumber;

    private String description;

    @Column(precision = 10, scale = 2)
    private BigDecimal previousSelicTarget;

    @Column(precision = 10, scale = 2)
    private BigDecimal selicTarget;

    private String minutesUrl;

    @CreationTimestamp
    @Column(nullable = false)
    LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(nullable = false)
    private LocalDateTime updatedAt;

    public BigDecimal getVariation() {
        if (selicTarget == null || previousSelicTarget == null) {
            return BigDecimal.ZERO;
        }

        return selicTarget.subtract(previousSelicTarget);
    }

    public CopomMeetingDecision getDecision() {
        if (selicTarget == null || previousSelicTarget == null) {
            return CopomMeetingDecision.UNAVAILABLE;
        }

        int comparison = selicTarget.compareTo(previousSelicTarget);

        if (comparison > 0) {
            return CopomMeetingDecision.Hike;
        } else if (comparison < 0) {
            return CopomMeetingDecision.CUT;
        } else {
            return CopomMeetingDecision.MAINTAIN;
        }

    }


}
