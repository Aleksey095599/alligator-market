package com.alligator.market.domain.marketdata.tick.level.source;

import com.alligator.market.domain.marketdata.tick.level.source.type.SourceTickType;
import com.alligator.market.domain.marketdata.tick.level.source.vo.SourceInstrumentCode;

import java.time.Instant;

public interface SourceTick {

    SourceTickType sourceTickType();

    SourceInstrumentCode sourceInstrumentCode();

    Instant sourceTickTime();
}
