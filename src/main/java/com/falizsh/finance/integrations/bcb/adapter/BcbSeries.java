package com.falizsh.finance.integrations.bcb.adapter;

public enum BcbSeries {
    SELIC_OVER_DAILY(11),
    SELIC_TARGET(432),
    CDI(12),
    IPCA_12M(433);

    private final long code;

    BcbSeries(long code) {
        this.code = code;
    }

    public long getCode() {
        return code;
    }
}
