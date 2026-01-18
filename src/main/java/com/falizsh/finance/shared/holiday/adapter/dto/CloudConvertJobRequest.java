package com.falizsh.finance.shared.holiday.adapter.dto;

import java.util.Map;

public record CloudConvertJobRequest(Map<String, Map<String, Object>> tasks) {

    public static CloudConvertJobRequest createForAnbima(String anbimaUrl) {

        return new CloudConvertJobRequest(Map.of(
                "import-anbima", Map.of(
                        "operation", "import/url",
                        "url", anbimaUrl
                ),
                "convert-csv", Map.of(
                        "operation", "convert",
                        "input", "import-anbima",
                        "output_format", "csv",
                        "engine", "office",
                        "input_format", "xls"
                ),
                "export-url", Map.of(
                        "operation", "export/url",
                        "input", "convert-csv",
                        "inline", false,
                        "archive_multiple_files", false
                )
        ));

    }
}
