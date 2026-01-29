package com.falizsh.finance.shared.bcbifdata.adapter.impl;

import com.falizsh.finance.infra.sourcefile.model.SourceFile;
import com.falizsh.finance.infra.sourcefile.model.SourceFileDomain;
import com.falizsh.finance.infra.sourcefile.model.SourceFileStatus;
import com.falizsh.finance.infra.sourcefile.repository.SourceFileRepository;
import com.falizsh.finance.infra.storage.FileStorageProvider;
import com.falizsh.finance.shared.bcbifdata.adapter.BacenSrapperService;
import com.microsoft.playwright.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class BacenScraperProvider implements BacenSrapperService {

    private final FileStorageProvider fileStorageProvider;
    private final SourceFileRepository sourceFileRepository;

    @Override
    public String scrapeSummary(String referenceDate) {
        log.info("Iniciando rotina de scraping do IFData para a data: {}", referenceDate);

        try (Playwright playwright = Playwright.create()) {

            Browser browser = playwright.chromium().launch(new BrowserType.LaunchOptions()
                    .setHeadless(false));

            BrowserContext context = browser.newContext();
            Page page = context.newPage();
            page.setDefaultTimeout(60000);

            log.info("Acessando IFData");
            page.navigate("https://www3.bcb.gov.br/ifdata/");

            log.info("Selecionando Data");
            page.click("#btnDataBase");
            page.click("ul#ulDataBase >> text=" + referenceDate);

            log.info("Selecionando Tipo de Instituição");
            page.click("#btnTipoInst");
            page.click("ul#ulTipoInst >> text=Conglomerados Prudenciais e Instituições Independentes");

            log.info("Selecionando Relatório...");
            page.click("#btnRelatorio");
            page.click("ul#ulRelatorio >> text=Resumo");

            log.info("Fazendo Download");
            page.waitForSelector("#aExportCsv");

            Download download = page.waitForDownload(() -> {
                page.click("#aExportCsv");
            });

            Path tempPath = download.path();
            byte[] content = Files.readAllBytes(tempPath);

            String originalName = "bacen_ifdata_resumo_" + referenceDate.replace("/", "-") + ".csv";

            String checksum = DigestUtils.md5DigestAsHex(content);

            String storagePath = fileStorageProvider.storageFile(originalName, content);

            createAndSaveSourceFile(originalName, storagePath, content.length, checksum, referenceDate);

            log.info("Arquivo salvo em {}", storagePath);

            browser.close();

            return storagePath;
        } catch (Exception e) {
            log.error("Erro ao realizar scraping do Bacen", e);
            throw new RuntimeException("Falha no processo de scraping: " + e.getMessage(), e);
        }
    }

    private void createAndSaveSourceFile(String fileName, String storagePath, long size, String checksum, String dataRef) {
        Map<String, Object> metadata = new HashMap<>();
        metadata.put("data_referencia", dataRef);
        metadata.put("origem", "AUTOMATED_SCRAPER");

        SourceFile sourceFile = SourceFile.builder()
                .fileName(fileName)
                .originalFileName(fileName)
                .storagePath(storagePath)
                .domain(SourceFileDomain.IF_DATA_SUMMARY)
                .fileSize(size)
                .checksum(checksum)
                .contentType("text/csv")
                .status(SourceFileStatus.PROCESSED)
                .metadata(metadata)
                .user(null)
                .build();

        sourceFileRepository.save(sourceFile);
        log.info("Registro salvo na tabela source_file com ID: {}", sourceFile.getId());
    }
}