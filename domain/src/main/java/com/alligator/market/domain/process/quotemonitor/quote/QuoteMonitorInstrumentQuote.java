package com.alligator.market.domain.process.quotemonitor.quote;

import com.alligator.market.domain.instrument.vo.InstrumentCode;
import com.alligator.market.domain.source.vo.SourceCode;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Objects;

public record QuoteMonitorInstrumentQuote(
        InstrumentCode instrumentCode,
        SourceCode sourceCode,
        BigDecimal lastPrice,
        Instant sourceTickTime,
        Instant receivedAt
) {

    public QuoteMonitorInstrumentQuote {
        Objects.requireNonNull(instrumentCode, "instrumentCode must not be null");
        Objects.requireNonNull(sourceCode, "sourceCode must not be null");
        Objects.requireNonNull(lastPrice, "lastPrice must not be null");
        Objects.requireNonNull(sourceTickTime, "sourceTickTime must not be null");
        Objects.requireNonNull(receivedAt, "receivedAt must not be null");

        if (lastPrice.signum() <= 0) {
            throw new IllegalArgumentException("lastPrice must be positive");
        }
    }
}
