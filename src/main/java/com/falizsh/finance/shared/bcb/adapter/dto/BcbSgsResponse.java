package com.falizsh.finance.shared.bcb.adapter.dto;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record BcbSgsResponse(
        @JsonAlias("data")
        String date,

        @JsonAlias("valor")
        String value
) {
}
