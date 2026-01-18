package com.falizsh.finance.infra.storage;

public interface FileStorageProvider {

    String storageFile(String fileName, byte[] content);

}
