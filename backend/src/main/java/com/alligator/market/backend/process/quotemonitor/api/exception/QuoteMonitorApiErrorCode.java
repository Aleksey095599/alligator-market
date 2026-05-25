package com.alligator.market.backend.process.quotemonitor.api.exception;

public enum QuoteMonitorApiErrorCode {
    DUPLICATE_INSTRUMENT_CODE,
    INSTRUMENT_CANDIDATE_NOT_FOUND,
    INSTRUMENT_SELECTION_LOCKED;

    public String code() {
        return name();
    }
}
