package com.falizsh.finance.infra.file;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@Service
public class FileStorageService {

    private final Path fileStorageLocations = Paths.get(System.getProperty("java.io.tmpdir"));

    public String storeFile(MultipartFile file) {
        try {

            String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
            Path targetLocation = this.fileStorageLocations.resolve(fileName);
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
            return targetLocation.toString();

        } catch (IOException e) {
            throw new RuntimeException("Failed.on.store.archive");
        }
    }

    public Path loadFile(String filePath) {
        return Paths.get(filePath);
    }

}
