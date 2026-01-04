package com.falizsh.finance.shared.bcb.adapter;

public enum BcbSeries {
    SELIC(11),
    CDI(12),
    IPCA(433);

    private final long code;

    BcbSeries(long code) {
        this.code = code;
    }

    public long getCode() {
        return code;
    }
}
