package com.alligator.market.domain.marketdata.tick.level.capture;

import com.alligator.market.domain.instrument.vo.InstrumentCode;
import com.alligator.market.domain.marketdata.tick.level.capture.vo.QuoteCollectionStreamCode;
import com.alligator.market.domain.marketdata.tick.level.source.SourceMarketDataTick;
import com.alligator.market.domain.provider.vo.ProviderCode;

import java.time.Instant;
import java.util.Objects;

/**
 * Captured-level рыночный тик, полученный с помощью добавления к {@link SourceMarketDataTick} полей,
 * содержащих метаданные приложения на момент "захвата" тика.
 *
 * @param collectionStreamCode код потока сбора котировок
 * @param instrumentCode       внутренний код инструмента приложения
 * @param providerCode         код провайдера рыночных данных
 * @param sourceTick           source-level тик, полученный от источника
 * @param receivedTimestamp    момент получения или фиксации тика приложением
 */
public record CapturedMarketDataTick(
        QuoteCollectionStreamCode collectionStreamCode,
        InstrumentCode instrumentCode,
        ProviderCode providerCode,
        SourceMarketDataTick sourceTick,
        Instant receivedTimestamp
) {
    public CapturedMarketDataTick {
        Objects.requireNonNull(collectionStreamCode, "collectionStreamCode must not be null");
        Objects.requireNonNull(instrumentCode, "instrumentCode must not be null");
        Objects.requireNonNull(providerCode, "providerCode must not be null");
        Objects.requireNonNull(sourceTick, "sourceTick must not be null");
        Objects.requireNonNull(receivedTimestamp, "receivedTimestamp must not be null");
    }
}
