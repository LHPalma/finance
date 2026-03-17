package com.falizsh.finance.infrastructure.sourcefile.action;

import com.falizsh.finance.infrastructure.sourcefile.model.SourceFile;
import com.falizsh.finance.infrastructure.sourcefile.model.SourceFileDomain;
import com.falizsh.finance.infrastructure.sourcefile.model.SourceFileStatus;
import com.falizsh.finance.infrastructure.sourcefile.repository.SourceFileCommand;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class AuditSourceFileAction {

    private final SourceFileCommand sourceFileCommand;

    public SourceFile start(String fileName, byte[] content, String mimeType, SourceFileDomain domain) {
        return sourceFileCommand.create(fileName, content, mimeType, domain, null, null);
    }

    public void fail(SourceFile sourceFile, String reason) {
        sourceFileCommand.addError(sourceFile, reason, null);
        sourceFileCommand.updateStatus(sourceFile, SourceFileStatus.FAILED);
    }

    public void complete(SourceFile sourceFile) {
        sourceFileCommand.updateStatus(sourceFile, SourceFileStatus.PROCESSED);
    }
}