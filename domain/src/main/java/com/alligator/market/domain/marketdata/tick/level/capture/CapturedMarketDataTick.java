package com.alligator.market.domain.marketdata.tick.level.capture;

import com.alligator.market.domain.instrument.vo.InstrumentCode;
import com.alligator.market.domain.marketdata.capture.process.vo.MarketDataCaptureProcessCode;
import com.alligator.market.domain.marketdata.tick.level.source.SourceMarketDataTick;
import com.alligator.market.domain.source.vo.MarketDataSourceCode;

import java.time.Instant;
import java.util.Objects;

/**
 * Captured-level рыночный тик, полученный с помощью добавления к {@link SourceMarketDataTick} полей,
 * содержащих метаданные приложения на момент захвата тика.
 *
 * @param captureProcessCode код процесса захвата рыночных данных
 * @param instrumentCode        внутренний код инструмента приложения
 * @param sourceCode            код источника рыночных данных
 * @param sourceTick            source-level тик, полученный от источника
 * @param receivedTimestamp     момент получения или захвата тика приложением
 */
public record CapturedMarketDataTick(
        MarketDataCaptureProcessCode captureProcessCode,
        InstrumentCode instrumentCode,
        MarketDataSourceCode sourceCode,
        SourceMarketDataTick sourceTick,
        Instant receivedTimestamp
) {
    public CapturedMarketDataTick {
        Objects.requireNonNull(captureProcessCode, "captureProcessCode must not be null");
        Objects.requireNonNull(instrumentCode, "instrumentCode must not be null");
        Objects.requireNonNull(sourceCode, "sourceCode must not be null");
        Objects.requireNonNull(sourceTick, "sourceTick must not be null");
        Objects.requireNonNull(receivedTimestamp, "receivedTimestamp must not be null");
    }
}
