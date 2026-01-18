package com.falizsh.finance.shared.holiday.adapter.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record CloudConvertJobResponse(Data data) {

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Data(String id, String status, List<Task> tasks) {
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Task(String name, String status, Result result) {
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Result(List<File> files) {
    }


    @JsonIgnoreProperties(ignoreUnknown = true)
    public record File(String url) {
    }

    public String findExportUrl() {
        if (data == null || data.tasks == null) {
            return null;
        }

        return data.tasks.stream()
                .filter(t -> "export-url".equals(t.name) && "finished".equals(t.status()))
                .findFirst()
                .map(t -> t.result().files().getFirst().url())
                .orElse(null);

    }


}
