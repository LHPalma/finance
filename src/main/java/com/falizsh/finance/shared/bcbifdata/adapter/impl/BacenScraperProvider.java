package com.falizsh.finance.shared.bcbifdata.adapter.impl;

import com.falizsh.finance.infra.storage.FileStorageProvider;
import com.falizsh.finance.shared.bcbifdata.adapter.BacenSrapperService;
import com.microsoft.playwright.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.nio.file.Files;
import java.nio.file.Path;

@Slf4j
@Service
@RequiredArgsConstructor
public class BacenScraperProvider implements BacenSrapperService {

    private final FileStorageProvider fileStorageProvider;

    @Override
    public String scrapeResumo(String referenceDate) {
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

            String fileName = "bacen_ifdata_resumo_" + referenceDate.replace("/", "-") + ".csv";

            String savedPath = fileStorageProvider.storageFile(fileName, content);

            log.info("Arquivo salvo em {}", savedPath);

            browser.close();

            return savedPath;
        } catch (Exception e) {
            log.error("Erro ao realizar scraping do Bacen", e);
            throw new RuntimeException("Falha no processo de scraping: " + e.getMessage(), e);
        }
    }
}