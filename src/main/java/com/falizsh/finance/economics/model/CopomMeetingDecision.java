package com.falizsh.finance.economics.model;

public enum CopomMeetingDecision {
    Hike("Aumento"),
    CUT("Redução"),
    MAINTAIN("Manutenção"),
    UNAVAILABLE("Não disponível");

    private final String displayName;

    CopomMeetingDecision(String name) {
        this.displayName = name;
    }

}
