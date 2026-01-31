package com.falizsh.finance.infra.sourcefile.model;

import jakarta.persistence.*;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.annotations.*;
import org.hibernate.type.SqlTypes;

import java.time.Instant;
import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity(name = "SourceFileError")
@Table(name = "source_file_error", schema = "finance", indexes = {@Index(name = "idx_error_source_file",
        columnList = "source_file_id")})
public class SourceFileError {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "source_file_id", nullable = false)
    private SourceFile sourceFile;

    @NotNull
    @Lob
    @Column(name = "error_message", nullable = false, columnDefinition = "TEXT")
    private String errorMessage;

    @Size(max = 255)
    @Column(name = "location_reference")
    private String locationReference;

    @Lob
    @Column(name = "raw_data", columnDefinition = "TEXT")
    private String rawData;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "error_details")
    private Map<String, Object> errorDetails;

    @CreationTimestamp
    @ColumnDefault("CURRENT_TIMESTAMP")
    @Column(name = "created_at", nullable = false)
    private Instant createdAt;


}