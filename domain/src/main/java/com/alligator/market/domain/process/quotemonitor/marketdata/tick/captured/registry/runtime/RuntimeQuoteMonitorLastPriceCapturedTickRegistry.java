package com.alligator.market.domain.process.quotemonitor.marketdata.tick.captured.registry.runtime;

import com.alligator.market.domain.instrument.vo.InstrumentCode;
import com.alligator.market.domain.process.quotemonitor.marketdata.tick.captured.QuoteMonitorLastPriceCapturedTick;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface RuntimeQuoteMonitorLastPriceCapturedTickRegistry {

    Optional<QuoteMonitorLastPriceCapturedTick> findByInstrumentCode(InstrumentCode instrumentCode);

    List<QuoteMonitorLastPriceCapturedTick> currentTicks();

    Map<InstrumentCode, QuoteMonitorLastPriceCapturedTick> ticksByInstrumentCode();
}
