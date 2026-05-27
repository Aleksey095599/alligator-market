package com.alligator.market.domain.process.quotemonitor.marketdata.tick;

import com.alligator.market.domain.capturer.vo.CapturerCode;
import com.alligator.market.domain.instrument.vo.InstrumentCode;
import com.alligator.market.domain.marketdata.tick.level.captured.CapturedTick;
import com.alligator.market.domain.marketdata.tick.level.source.type.SourceLastPriceTick;
import com.alligator.market.domain.process.quotemonitor.capturer.QuoteMonitorCapturer;
import com.alligator.market.domain.source.vo.SourceCode;

import java.time.Instant;
import java.util.Objects;

public record QuoteMonitorLastPriceCapturedTick(
        InstrumentCode instrumentCode,
        SourceCode sourceCode,
        SourceLastPriceTick sourceTick,
        Instant receivedTimestamp
) implements CapturedTick {

    public QuoteMonitorLastPriceCapturedTick {
        Objects.requireNonNull(instrumentCode, "instrumentCode must not be null");
        Objects.requireNonNull(sourceCode, "sourceCode must not be null");
        Objects.requireNonNull(sourceTick, "sourceTick must not be null");
        Objects.requireNonNull(receivedTimestamp, "receivedTimestamp must not be null");
    }

    @Override
    public InstrumentCode instrumentCode() {
        return instrumentCode;
    }

    @Override
    public CapturerCode capturerCode() {
        return QuoteMonitorCapturer.CAPTURER_CODE;
    }

    @Override
    public SourceCode sourceCode() {
        return sourceCode;
    }

    @Override
    public SourceLastPriceTick sourceTick() {
        return sourceTick;
    }

    @Override
    public Instant receivedTimestamp() {
        return receivedTimestamp;
    }
}
