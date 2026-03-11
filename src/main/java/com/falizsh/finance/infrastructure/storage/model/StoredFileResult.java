package com.falizsh.finance.infrastructure.storage.model;

public record StoredFileResult(
        String storagePath,
        String checksum,
        long contentLength
) {
}
