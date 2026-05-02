package com.alligator.market.domain.marketdata.tick.source;

import com.alligator.market.domain.marketdata.tick.source.vo.SourceInstrumentCode;

import java.time.Instant;

public sealed interface SourceMarketDataTick
        permits SourceLastPriceTick, SourceTopOfBookQuoteTick {

    SourceInstrumentCode sourceInstrumentCode();

    Instant sourceTimestamp();
}
