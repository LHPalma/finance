package com.falizsh.finance.infra.storage.impl;

import com.falizsh.finance.infra.storage.FileStorageProvider;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

@Slf4j
@Component
public class LocalFileSystemStorageProvider implements FileStorageProvider {

    private final Path rootLocation;

    public LocalFileSystemStorageProvider(@Value("${storage.local.location:./local-bucket}") String storageLocation) {
        this.rootLocation = Paths.get(storageLocation);
        init();
    }

    private void init() {
        try {
            Files.createDirectories(rootLocation);
        } catch (IOException e) {
            throw new RuntimeException("Não foi possível inicializar o diretório de storage", e);
        }
    }

    @Override
    public String storageFile(String fileName, byte[] content) {
        try {
            String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy_MM_dd-HH_mm_ss"));
            String uuid = UUID.randomUUID().toString();
            String finalFileName = timestamp + "-" + uuid + "-" + fileName;

            Path destinationFile = this.rootLocation.resolve(finalFileName);

            Files.write(destinationFile, content);

            log.info("Arquivo salvo no bucket local: {}", destinationFile.toAbsolutePath());
            return destinationFile.toString();

        } catch (IOException e) {
            log.error("Falha ao arquivar arquivo", e);
            throw new RuntimeException("Falha ao armazenar arquivo " + fileName, e);
        }
    }
}
