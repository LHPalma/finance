package com.falizsh.finance.shared.bcbifdata.adapter.impl;

import com.falizsh.finance.shared.bcbifdata.adapter.BacenIfDataService;
import com.falizsh.finance.shared.bcbifdata.adapter.BcbIfDataClient;
import com.falizsh.finance.shared.bcbifdata.dto.BaselIndexResponse;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Slf4j
@Service
@RequiredArgsConstructor
public class BacenIfDataProvider implements BacenIfDataService {

    private final BcbIfDataClient bcbClient;
    private final ObjectMapper objectMapper;

    private static final String DEFAULT_REPORT_NAME = "Resumo";

    @Override
    public BaselIndexResponse fetchBaselIndex(String instituicao, String dataReferencia) {
        log.info("Orquestrando busca do Índice de Basileia para {} em {}", instituicao, dataReferencia);

        try {
            Integer dataFormatada = formatData(dataReferencia);

            JsonNode instNode = fetchInstitutionNode(instituicao, dataFormatada);
            String nomeCompleto = instNode.get("NomeInstituicao").asText();

            String idPrimario = resolveInstitutionId(instNode);
            log.info("Instituição: {} | ID Primário: {}", nomeCompleto, idPrimario);

            int reportNumber = fetchReportNumberByName(DEFAULT_REPORT_NAME);

            JsonNode valuesArray = fetchFinancialValues(instNode, dataFormatada, reportNumber, idPrimario);

            BigDecimal indiceBasileia = findSaldoByColumnName(valuesArray, "Basileia");
            BigDecimal indiceImobilizacao = findSaldoByColumnName(valuesArray, "Imobiliza");

            return BaselIndexResponse.builder()
                    .institution(nomeCompleto)
                    .referenceDate(dataReferencia)
                    .baselIndex(indiceBasileia)
                    .immobilizationIndex(indiceImobilizacao)
                    .build();

        } catch (Exception e) {
            log.error("Erro na integração com Bacen", e);
            throw new RuntimeException("Falha ao buscar dados: " + e.getMessage(), e);
        }
    }

    private Integer formatData(String dataReferencia) {
        String[] parts = dataReferencia.split("/");
        return Integer.parseInt(parts[1] + parts[0]);
    }

    private JsonNode fetchInstitutionNode(String institution, int formatedDate) {

        if (institution == null ||  institution.isEmpty()) {
            throw new IllegalArgumentException("Nome da instituição não pode ser vazio: " + institution);
        }

        try {
            String filterExact = "NomeInstituicao eq '" + institution.toUpperCase() + "'";
            JsonNode exactNode = fetchAndGetFirst(bcbClient.fetchInstituition(formatedDate, filterExact));

            if (exactNode != null) {
                return exactNode;
            }

            log.info("Busca exata não retornou resultados para '{}'. Tentando contains", institution);
            String filterContains = "contains(NomeInstituicao,'" + institution.toUpperCase() + "')";
            JsonNode containsNode = fetchAndGetFirst(bcbClient.fetchInstituition(formatedDate, filterContains));

            if (containsNode == null) {
                throw new RuntimeException("Instituição não encontrada: " + institution);
            }

            return containsNode;

        } catch (Exception e) {
            throw new RuntimeException("Erro ao buscar instituição: " + e.getMessage(), e);
        }
    }

    private String resolveInstitutionId(JsonNode instNode) {
        if (instNode.hasNonNull("CodConglomeradoPrudencial")) {
            return instNode.get("CodConglomeradoPrudencial").asText();
        }
        return instNode.get("CodInst").asText();
    }

    private int fetchReportNumberByName(String name) {
        try {
            JsonNode relNode = null;

            String filterExact = "NomeRelatorio eq '" + name + "'";
            try {
                relNode = fetchAndGetFirst(bcbClient.fetchReports(filterExact));
            } catch (Exception e) {
                log.warn("Erro na busca exata do relatório '{}': {}", name, e.getMessage());
            }

            if (relNode == null) {
                log.info("Relatório exato '{}' não encontrado. Tentando busca parcial (contains)...", name);
                String filterContains = "contains(NomeRelatorio, '" + name + "')";
                relNode = fetchAndGetFirst(bcbClient.fetchReports(filterContains));
            }

            if (relNode == null) {
                throw new RuntimeException("Relatório '" + name + "' não encontrado na API do Bacen (nem exato, nem parcial).");
            }

            int reportNumber;
            if (relNode.has("NumeroRelatorio")) {
                reportNumber = relNode.get("NumeroRelatorio").asInt();
            } else if (relNode.has("CodRelatorio")) {
                reportNumber = relNode.get("CodRelatorio").asInt();
            } else {
                throw new RuntimeException("O relatório foi encontrado, mas não possui campo de ID (NumeroRelatorio ou CodRelatorio). Campos: " + relNode.fieldNames());
            }

            log.info("Relatório localizado: '{}' (Busca: '{}') -> ID: {}", relNode.get("NomeRelatorio").asText(), name, reportNumber);
            return reportNumber;

        } catch (Exception e) {
            log.error("Erro fatal ao buscar relatório '{}'", name, e);
            throw new RuntimeException("Erro ao buscar ID do relatório '" + name + "': " + e.getMessage(), e);
        }
    }

    private JsonNode fetchFinancialValues(JsonNode instNode, int dataFormatada, int reportNumber, String idPrimario) throws Exception {
        Integer tipoInst = 1;
        String relatorioParam = "'" + reportNumber + "'";
        String filterValores = "CodInst eq '" + idPrimario + "'";

        String jsonResponse = bcbClient.fetchValues(dataFormatada, tipoInst, relatorioParam, filterValores);
        JsonNode valuesArray = parseValueArray(jsonResponse);

        if (!valuesArray.isEmpty()) {
            return valuesArray;
        }

        String codInstOriginal = instNode.get("CodInst").asText();
        if (!codInstOriginal.equals(idPrimario)) {
            log.warn("Dados não encontrados para {}. Tentando fallback para CodInst original: {}", idPrimario, codInstOriginal);
            filterValores = "CodInst eq '" + codInstOriginal + "'";
            jsonResponse = bcbClient.fetchValues(dataFormatada, tipoInst, relatorioParam, filterValores);
            valuesArray = parseValueArray(jsonResponse);
        }

        if (valuesArray.isEmpty()) {
            throw new RuntimeException("Dados financeiros não encontrados.");
        }

        return valuesArray;
    }

    private JsonNode fetchAndGetFirst(String jsonResponse) throws Exception {
        JsonNode root = objectMapper.readTree(jsonResponse);
        JsonNode valueNode = root.get("value");
        if (valueNode != null && valueNode.isArray() && !valueNode.isEmpty()) {
            return valueNode.get(0);
        }
        return null;
    }

    private JsonNode parseValueArray(String jsonResponse) throws Exception {
        JsonNode root = objectMapper.readTree(jsonResponse);
        JsonNode valueNode = root.get("value");
        if (valueNode != null && valueNode.isArray()) {
            return valueNode;
        }
        return objectMapper.createArrayNode();
    }

    private BigDecimal findSaldoByColumnName(JsonNode array, String partialName) {
        if (array == null) return BigDecimal.ZERO;
        for (JsonNode node : array) {
            if (node.has("NomeColuna") && node.has("Saldo")) {
                String colName = node.get("NomeColuna").asText();
                if (colName != null && colName.toLowerCase().contains(partialName.toLowerCase())) {
                    return node.get("Saldo").decimalValue();
                }
            }
        }
        return BigDecimal.ZERO;
    }
}