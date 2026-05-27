package com.alligator.market.domain.marketdata.tick.level.captured;

import com.alligator.market.domain.instrument.vo.InstrumentCode;
import com.alligator.market.domain.capturer.vo.CapturerCode;
import com.alligator.market.domain.marketdata.tick.level.source.SourceTick;
import com.alligator.market.domain.source.vo.SourceCode;

import java.time.Instant;

public interface CapturedTick {

    CapturerCode capturerCode();

    InstrumentCode instrumentCode();

    SourceCode sourceCode();

    SourceTick sourceTick();

    Instant receivedTimestamp();
}
