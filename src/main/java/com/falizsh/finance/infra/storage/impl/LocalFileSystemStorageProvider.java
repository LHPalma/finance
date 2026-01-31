package com.falizsh.finance.infra.storage.impl;

import com.falizsh.finance.infra.storage.FileStorageProvider;
import com.falizsh.finance.infra.storage.model.StoredFileResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.MessageDigest;

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
    public StoredFileResult storageFile(String finalFileName, byte[] content) {
        try {
            Path destinationFile = this.rootLocation.resolve(finalFileName);

            Files.write(destinationFile, content);

            String checksum = calculateSha256(content);
            log.info("Arquivo salvo: {}", destinationFile.toAbsolutePath());

            return new StoredFileResult(
                    destinationFile.toString(),
                    checksum,
                    content.length
            );

        } catch (IOException e) {
            log.error("Falha ao arquivar arquivo: {}", finalFileName, e);
            throw new RuntimeException("Falha ao armazenar arquivo " + finalFileName, e);
        }
    }

    private String calculateSha256(byte[] content) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] encodedhash = digest.digest(content);
            StringBuilder hexString = new StringBuilder(2 * encodedhash.length);
            for (byte b : encodedhash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (Exception e) {
            throw new RuntimeException("Erro ao calcular checksum", e);
        }
    }
}