package com.alligator.market.domain.marketdata.tick.level.source;

import com.alligator.market.domain.marketdata.tick.level.source.vo.SourceInstrumentCode;

import java.time.Instant;

/**
 * Source-level рыночный тик от источника рыночных данных.
 */
public interface SourceMarketDataTick {

    /**
     * Идентификатор инструмента в системе источника рыночных данных.
     */
    SourceInstrumentCode sourceInstrumentCode();

    /**
     * Время рыночного тика в системе источника рыночных данных.
     */
    Instant sourceTimestamp();
}
