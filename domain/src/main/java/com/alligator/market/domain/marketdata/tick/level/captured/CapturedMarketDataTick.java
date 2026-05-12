package com.alligator.market.domain.marketdata.tick.level.captured;

import com.alligator.market.domain.instrument.vo.InstrumentCode;
import com.alligator.market.domain.capturer.vo.MarketDataCapturerCode;
import com.alligator.market.domain.marketdata.tick.level.source.SourceMarketDataTick;
import com.alligator.market.domain.source.vo.SourceCode;

import java.time.Instant;
import java.util.Objects;

/**
 * Captured tick with source data plus application capture metadata.
 */
public record CapturedMarketDataTick(
        MarketDataCapturerCode capturerCode,
        InstrumentCode instrumentCode,
        SourceCode sourceCode,
        SourceMarketDataTick sourceTick,
        Instant receivedTimestamp
) {
    public CapturedMarketDataTick {
        Objects.requireNonNull(capturerCode, "capturerCode must not be null");
        Objects.requireNonNull(instrumentCode, "instrumentCode must not be null");
        Objects.requireNonNull(sourceCode, "sourceCode must not be null");
        Objects.requireNonNull(sourceTick, "sourceTick must not be null");
        Objects.requireNonNull(receivedTimestamp, "receivedTimestamp must not be null");
    }
}
