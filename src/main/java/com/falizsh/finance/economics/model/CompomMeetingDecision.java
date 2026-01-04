package com.falizsh.finance.economics.model;

public enum CompomMeetingDecision {
    Hike("Aumento"),
    CUT("Redução"),
    MAINTAIN("Manutenção"),
    UNAVAILABLE("Não disponível");

    private final String displayName;

    CompomMeetingDecision(String name) {
        this.displayName = name;
    }

}
