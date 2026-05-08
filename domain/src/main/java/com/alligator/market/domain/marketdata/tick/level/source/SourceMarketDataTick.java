package com.alligator.market.domain.marketdata.tick.level.source;

import com.alligator.market.domain.marketdata.tick.level.source.type.SourceMarketDataTickType;
import com.alligator.market.domain.marketdata.tick.level.source.vo.SourceInstrumentCode;

import java.time.Instant;

/**
 * Market data tick as reported by an external source.
 */
public interface SourceMarketDataTick {

    SourceMarketDataTickType sourceTickType();

    SourceInstrumentCode sourceInstrumentCode();

    Instant sourceTimestamp();
}
