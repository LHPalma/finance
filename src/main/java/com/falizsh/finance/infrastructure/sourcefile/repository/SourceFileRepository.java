package com.falizsh.finance.infrastructure.sourcefile.repository;

import com.falizsh.finance.infrastructure.sourcefile.model.SourceFile;
import com.falizsh.finance.infrastructure.sourcefile.model.SourceFileDomain;
import com.falizsh.finance.infrastructure.sourcefile.model.SourceFileStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SourceFileRepository extends JpaRepository<SourceFile, Long> {

    List<SourceFile> findByStatus(SourceFileStatus status);

    Optional<SourceFile> findById(Long id);

    Optional<SourceFile> findByChecksum(String checksum);

    List<SourceFile> findByDomain(SourceFileDomain domain);

}