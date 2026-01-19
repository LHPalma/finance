package com.falizsh.finance.infra.sourcefile.repository;

import com.falizsh.finance.infra.sourcefile.model.SourceFile;
import com.falizsh.finance.infra.sourcefile.model.SourceFileDomain;
import com.falizsh.finance.infra.sourcefile.model.SourceFileStatus;
import com.falizsh.finance.infra.storage.FileStorageProvider;
import com.falizsh.finance.users.user.model.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

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
            String filename,
            byte[] content,
            String contentType,
            SourceFileDomain domain,
            User user,
            Map<String, Object> metadata
    ) {

        validateUserDomainConsistency(domain, user);

        String storagePath = storageProvider.storageFile(
                filename,
                content
        );

        SourceFile sourceFile = SourceFile.builder()
                .fileName(filename)
                .originalFileName(filename)
                .storagePath(storagePath)
                .contentType(contentType)
                .domain(domain)
                .user(user)
                .metadata(metadata)
                .fileSize((long) content.length)
                .build();

        return repository.save(sourceFile);
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
