package com.alligator.market.backend.process.quotemonitor.application.instrument.exception;

import com.alligator.market.domain.instrument.vo.InstrumentCode;

import java.util.Collection;
import java.util.stream.Collectors;

public final class QuoteMonitorInstrumentCandidateNotFoundException extends IllegalArgumentException {
    public QuoteMonitorInstrumentCandidateNotFoundException(Collection<InstrumentCode> instrumentCodes) {
        super("Quote monitor instrument candidates not found: " + format(instrumentCodes));
    }

    private static String format(Collection<InstrumentCode> instrumentCodes) {
        return instrumentCodes.stream()
                .map(InstrumentCode::value)
                .collect(Collectors.joining(", "));
    }
}
