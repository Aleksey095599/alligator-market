package com.alligator.market.domain.process.quotemonitor.livequote.registry.runtime;

import com.alligator.market.domain.instrument.vo.InstrumentCode;
import com.alligator.market.domain.process.quotemonitor.livequote.QuoteMonitorLiveQuote;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface RuntimeQuoteMonitorLiveQuoteRegistry {

    Optional<QuoteMonitorLiveQuote> findByInstrumentCode(InstrumentCode instrumentCode);

    List<QuoteMonitorLiveQuote> currentQuotes();

    Map<InstrumentCode, QuoteMonitorLiveQuote> quotesByInstrumentCode();
}
