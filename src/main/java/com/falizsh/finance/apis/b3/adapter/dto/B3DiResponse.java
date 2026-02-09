package com.falizsh.finance.apis.b3.adapter.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record B3DiResponse(
        @JsonProperty("Scty") List<B3Security> securities
) {

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record B3Security(
            @JsonProperty("symb") String symbol,
            @JsonProperty("desc") String description,
            @JsonProperty("mkt") B3Market market,
            @JsonProperty("asset") B3Asset asset,
            @JsonProperty("SctyQtn") B3SecurityQuotation quotation
    ) {}

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record B3Market(
            @JsonProperty("cd") String code
    ) {}

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record B3Asset(
            @JsonProperty("code") String code,
            @JsonProperty("AsstSummry") B3AssetSummary summary
    ) {}

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record B3AssetSummary(
            @JsonProperty("mtrtyCode") String maturityDate,
            @JsonProperty("opnCtrcts") Long openContracts
    ) {}

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record B3SecurityQuotation(
            @JsonProperty("curPrc") Double currentPrice,
            @JsonProperty("prvsDayAdjstmntPric") Double previousAdjustmentPrice,
            @JsonProperty("opngPric") Double openingPrice,
            @JsonProperty("minPric") Double minPrice,
            @JsonProperty("maxPric") Double maxPrice
    ) {}
}