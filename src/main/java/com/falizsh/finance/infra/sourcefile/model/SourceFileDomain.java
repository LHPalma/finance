package com.falizsh.finance.infra.sourcefile.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum SourceFileDomain {

    HOLIDAYS("Importação de feriados", true),
    VNA("Importação de VNA", true),
    CDI("Importação de CDI", true);

    private final String displayName;
    private final boolean isSystem;

}
