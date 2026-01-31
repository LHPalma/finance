package com.falizsh.finance.infra.storage;

import com.falizsh.finance.infra.storage.model.StoredFileResult;

public interface FileStorageProvider {

    StoredFileResult storageFile(String fileName, byte[] content);

}
