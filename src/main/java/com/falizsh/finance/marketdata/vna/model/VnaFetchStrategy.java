package com.falizsh.finance.marketdata.vna.model;

public enum VnaFetchStrategy {
    LOCAL_ONLY,
    REMOTE_ONLY,
    FALLBACK_NO_SAVE,
    FALLBACK_AND_SAVE
}