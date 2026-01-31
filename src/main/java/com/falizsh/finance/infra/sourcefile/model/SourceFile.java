package com.falizsh.finance.infra.sourcefile.model;

import com.falizsh.finance.users.user.model.User;
import jakarta.persistence.*;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.*;
import org.hibernate.type.SqlTypes;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity(name = "SourceFile")
@Table(name = "source_file")
public class SourceFile {
    private final static SourceFileStatus DEFAULT_STATUS = SourceFileStatus.PENDING;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Size(max = 255)
    @NotNull
    @Column(name = "file_name", nullable = false)
    private String fileName;

    @Size(max = 255)
    @Column(name = "original_file_name")
    private String originalFileName;

    @Size(max = 500)
    @NotNull
    @Column(name = "storage_path", nullable = false, length = 500)
    private String storagePath;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "domain", nullable = false, length = 100)
    private SourceFileDomain domain;

    @Size(max = 100)
    @Column(name = "content_type", length = 100)
    private String contentType;

    @ManyToOne(fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "file_size")
    private Long fileSize;

    @Size(max = 64)
    @Column(name = "checksum", length = 64)
    private String checksum;

    @NotNull
    @ColumnDefault("'PENDING'")
    @Builder.Default
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private SourceFileStatus status = DEFAULT_STATUS;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "metadata")
    private Map<String, Object> metadata;

    @CreationTimestamp
    @ColumnDefault("CURRENT_TIMESTAMP")
    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    @UpdateTimestamp
    @ColumnDefault("CURRENT_TIMESTAMP")
    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    public void setStatus(SourceFileStatus status) {
        this.status = status;
    }

    public void setMetadata(Map<String, Object> metadata) {
        this.metadata = metadata;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Builder.Default
    @OneToMany(mappedBy = "sourceFile", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<SourceFileError> errors = new ArrayList<>();

    public void addError(SourceFileError error) {
        errors.add(error);
        error.setSourceFile(this);
    }

    public void removeError(SourceFileError error) {
        errors.remove(error);
        error.setSourceFile(null);
    }

}
