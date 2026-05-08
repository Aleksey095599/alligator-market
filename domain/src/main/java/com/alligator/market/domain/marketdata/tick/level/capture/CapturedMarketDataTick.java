package com.alligator.market.domain.marketdata.tick.level.capture;

import com.alligator.market.domain.instrument.vo.InstrumentCode;
import com.alligator.market.domain.marketdata.capturer.vo.MarketDataCapturerCode;
import com.alligator.market.domain.marketdata.tick.level.source.SourceMarketDataTick;
import com.alligator.market.domain.source.vo.MarketDataSourceCode;

import java.time.Instant;
import java.util.Objects;

/**
 * Captured-level market data tick built from a source-level tick plus application capture metadata.
 *
 * @param capturerCode      code of the capturer that produced the tick
 * @param instrumentCode    instrument code
 * @param sourceCode        market data source code
 * @param sourceTick        source-level tick received from the source
 * @param receivedTimestamp time when the tick was received by the application
 */
public record CapturedMarketDataTick(
        MarketDataCapturerCode capturerCode,
        InstrumentCode instrumentCode,
        MarketDataSourceCode sourceCode,
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
