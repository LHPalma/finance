package com.falizsh.finance.infra.sourcefile.repository;

import com.falizsh.finance.infra.sourcefile.model.SourceFile;
import com.falizsh.finance.infra.sourcefile.model.SourceFileDomain;
import com.falizsh.finance.infra.sourcefile.model.SourceFileError;
import com.falizsh.finance.infra.sourcefile.model.SourceFileStatus;
import com.falizsh.finance.infra.storage.FileStorageProvider;
import com.falizsh.finance.infra.storage.model.StoredFileResult;
import com.falizsh.finance.users.user.model.User;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.text.Normalizer;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.regex.Pattern;

@Slf4j
@Service
@RequiredArgsConstructor
public class SourceFileCommand {

    private final SourceFileRepository repository;
    private final FileStorageProvider storageProvider;

    @Transactional
    public SourceFile uploadSystem(MultipartFile file, SourceFileDomain domain, Map<String, Object> metadata) {
        try {
            return create(
                    file.getOriginalFilename(),
                    file.getBytes(),
                    file.getContentType(),
                    domain,
                    null,
                    metadata
            );
        } catch (IOException e) {
            throw new RuntimeException("Erro ao ler os bytes do upload", e);
        }
    }

    @Transactional
    public SourceFile uploadUser(MultipartFile file, SourceFileDomain domain, User user, Map<String, Object> metadata) {

        try {
            return create(
                    file.getOriginalFilename(),
                    file.getBytes(),
                    file.getContentType(),
                    domain,
                    user,
                    metadata
            );
        } catch (IOException e) {
            throw new RuntimeException("Erro ao ler os bytes do upload", e);
        }

    }

    @Transactional
    public SourceFile create(
            String originalFileName,
            byte[] content,
            String contentType,
            SourceFileDomain domain,
            User user,
            Map<String, Object> metadata
    ) {
        validateUserDomainConsistency(domain, user);

        String finalFilename = generateUniqueFileName(originalFileName);

        StoredFileResult result = storageProvider.storageFile(finalFilename, content);

        SourceFile sourceFile = SourceFile.builder()
                .fileName(finalFilename)
                .originalFileName(originalFileName)
                .storagePath(result.storagePath())
                .checksum(result.checksum())
                .fileSize(result.contentLength())
                .contentType(contentType)
                .domain(domain)
                .user(user)
                .metadata(metadata)
                .status(SourceFileStatus.PENDING)
                .build();

        return repository.save(sourceFile);

    }

    private String generateUniqueFileName(String originalFileName) {
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy_MM_dd-HH_mm_ss"));
        String uuid = UUID.randomUUID().toString();
        String sanitizedName = sanitizeFileName(originalFileName);

        return String.format("%s-%s-%s", timestamp, uuid, sanitizedName);
    }

    /**
     *
     * @param originalFileName
     * @return Nome sanitizado sem caracteres especiais, espaços e acentos.
     */
    private String sanitizeFileName(String originalFileName) {
        if (originalFileName == null || originalFileName.isBlank()) {
            return "unnamed_file";
        }

        String extension = "";
        String nameWithoutExtension = originalFileName;
        int lastDotIndex = originalFileName.lastIndexOf(".");

        if (lastDotIndex > 0) {
            extension = originalFileName.substring(lastDotIndex); // ex: ".xml"
            nameWithoutExtension = originalFileName.substring(0, lastDotIndex); // ex: "meu_arquivo"
        }

        String normalized = Normalizer.normalize(nameWithoutExtension, Normalizer.Form.NFD);
        Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
        String noAccents = pattern.matcher(normalized).replaceAll("");

        String sanitizedName = noAccents.replaceAll("\\s+", "_")
                .replaceAll("[^a-zA-Z0-9_-]", "");

        final int MAX_FILE_NAME_LENGTH = 100;
        int maxNameLength = MAX_FILE_NAME_LENGTH - extension.length();
        if (maxNameLength < 1) maxNameLength = 1;

        if (sanitizedName.length() > maxNameLength) {
            sanitizedName = sanitizedName.substring(0, maxNameLength);
        }

        return sanitizedName + extension;
    }


    @Transactional
    public void updateStatus(SourceFile sourceFile, SourceFileStatus status) {
        sourceFile.setStatus(status);
        repository.save(sourceFile);
    }

    @Transactional
    public void addError(SourceFile sourceFile, String errorMsg, String rawData) {
        SourceFile managedSourceFile = repository.findById(sourceFile.getId())
                .orElseThrow(() -> new EntityNotFoundException("SourceFile.not.found: " + sourceFile.getId()));

        SourceFileError error = SourceFileError.builder()
                .errorMessage(errorMsg)
                .rawData(rawData)
                .sourceFile(managedSourceFile)
                .build();

        managedSourceFile.addError(error);
        repository.save(managedSourceFile);
    }


    @Transactional
    public void complete(Long id, int itemsProcessed) {
        repository.findById(id).ifPresent(file -> {
            file.setStatus(SourceFileStatus.PROCESSED);

            Map<String, Object> currentMeta = file.getMetadata();
            if (currentMeta != null) {
                Map<String, Object> newMeta = new HashMap<>(currentMeta);
                newMeta.put("processing_result", "success");
                newMeta.put("items_saved", itemsProcessed);
                file.setMetadata(newMeta);
            }

            repository.save(file);
        });
    }

    private void validateUserDomainConsistency(SourceFileDomain domain, User user) {
        if (domain.isSystem() && user != null) {
            throw new IllegalArgumentException(
                    "O domínio " + domain.name() + "é exclusivo do sistema e não pode ser vinculado a um usuário (" + user.getId() + ")."
            );
        }

        if (!domain.isSystem() && user == null) {
            throw new IllegalArgumentException("O domínio " + domain.name() + ", mas nenhum foi fornecido.");
        }
    }

}
