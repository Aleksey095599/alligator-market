package com.alligator.market.domain.marketdata.tick.level.captured;

import com.alligator.market.domain.instrument.vo.InstrumentCode;
import com.alligator.market.domain.capturer.vo.CapturerCode;
import com.alligator.market.domain.marketdata.tick.level.source.SourceTick;
import com.alligator.market.domain.source.vo.SourceCode;

import java.time.Instant;
import java.util.Objects;

public record CapturedTick(
        CapturerCode capturerCode,
        InstrumentCode instrumentCode,
        SourceCode sourceCode,
        SourceTick sourceTick,
        Instant receivedTimestamp
) {
    public CapturedTick {
        Objects.requireNonNull(capturerCode, "capturerCode must not be null");
        Objects.requireNonNull(instrumentCode, "instrumentCode must not be null");
        Objects.requireNonNull(sourceCode, "sourceCode must not be null");
        Objects.requireNonNull(sourceTick, "sourceTick must not be null");
        Objects.requireNonNull(receivedTimestamp, "receivedTimestamp must not be null");
    }
}
