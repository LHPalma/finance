package com.falizsh.finance.infra.storage.model;

public record StoredFileResult(
        String storagePath,
        String checksum,
        long contentLength
) {
}
