package com.alligator.market.domain.process.quotemonitor.quote.registry.runtime;

import com.alligator.market.domain.instrument.vo.InstrumentCode;
import com.alligator.market.domain.process.quotemonitor.quote.QuoteMonitorInstrumentQuote;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface RuntimeQuoteMonitorLiveQuoteRegistry {

    Optional<QuoteMonitorInstrumentQuote> findByInstrumentCode(InstrumentCode instrumentCode);

    List<QuoteMonitorInstrumentQuote> currentQuotes();

    Map<InstrumentCode, QuoteMonitorInstrumentQuote> quotesByInstrumentCode();
}
