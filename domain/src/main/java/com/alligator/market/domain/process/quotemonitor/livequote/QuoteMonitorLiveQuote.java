package com.alligator.market.domain.process.quotemonitor.livequote;

import com.alligator.market.domain.instrument.vo.InstrumentCode;
import com.alligator.market.domain.source.vo.SourceCode;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Objects;

public record QuoteMonitorLiveQuote(
        InstrumentCode instrumentCode,
        SourceCode sourceCode,
        BigDecimal lastPrice,
        Instant sourceTimestamp,
        Instant receivedAt
) {

    public QuoteMonitorLiveQuote {
        Objects.requireNonNull(instrumentCode, "instrumentCode must not be null");
        Objects.requireNonNull(sourceCode, "sourceCode must not be null");
        Objects.requireNonNull(lastPrice, "lastPrice must not be null");
        Objects.requireNonNull(sourceTimestamp, "sourceTimestamp must not be null");
        Objects.requireNonNull(receivedAt, "receivedAt must not be null");

        if (lastPrice.signum() <= 0) {
            throw new IllegalArgumentException("lastPrice must be positive");
        }
    }
}
