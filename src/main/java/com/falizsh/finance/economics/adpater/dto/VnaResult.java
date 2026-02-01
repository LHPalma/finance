package com.falizsh.finance.economics.adpater.dto;

import com.falizsh.finance.economics.model.Vna;
import java.util.Collections;
import java.util.List;

public record VnaResult(
        List<Vna> vnas,
        byte[] rawContent,
        boolean isSuccess,
        String failureReason
) {
    public static VnaResult success(List<Vna> vnas, byte[] rawContent) {
        return new VnaResult(vnas, rawContent, true, null);
    }

    public static VnaResult failure(byte[] rawContent, String reason) {
        return new VnaResult(Collections.emptyList(), rawContent, false, reason);
    }
}