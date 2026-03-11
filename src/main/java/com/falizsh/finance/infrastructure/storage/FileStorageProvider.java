package com.falizsh.finance.infrastructure.storage;

import com.falizsh.finance.infrastructure.storage.model.StoredFileResult;

public interface FileStorageProvider {

    StoredFileResult storageFile(String fileName, byte[] content);

}
