package com.falizsh.finance.infra.sourcefile.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum SourceFileDomain {

    HOLIDAYS("Importação de feriados", true),
    VNA("Importação de VNA", true),
    CDI("Importação de CDI", true),
    IF_DATA_SUMMARY("Relatório Resumo IFData", true);


    private final String displayName;
    private final boolean isSystem;

}
