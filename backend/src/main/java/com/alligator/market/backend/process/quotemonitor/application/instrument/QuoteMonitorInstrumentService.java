package com.alligator.market.backend.process.quotemonitor.application.instrument;

import com.alligator.market.backend.process.quotemonitor.application.instrument.exception.DuplicateQuoteMonitorInstrumentCodeException;
import com.alligator.market.backend.process.quotemonitor.application.instrument.exception.QuoteMonitorSourcePlanNotFoundException;
import com.alligator.market.backend.process.quotemonitor.application.instrument.port.QuoteMonitorInstrumentPort;
import com.alligator.market.domain.instrument.vo.InstrumentCode;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

public final class QuoteMonitorInstrumentService {
    private final QuoteMonitorInstrumentPort port;

    public QuoteMonitorInstrumentService(QuoteMonitorInstrumentPort port) {
        this.port = Objects.requireNonNull(port, "port must not be null");
    }

    public List<InstrumentCode> findAvailableInstrumentCodes() {
        return port.findAvailableInstrumentCodes();
    }

    public List<InstrumentCode> findSelectedInstrumentCodes() {
        return port.findSelectedInstrumentCodes();
    }

    public void replaceSelectedInstrumentCodes(List<InstrumentCode> instrumentCodes) {
        Objects.requireNonNull(instrumentCodes, "instrumentCodes must not be null");

        List<InstrumentCode> normalizedInstrumentCodes = instrumentCodes.stream()
                .map(instrumentCode -> Objects.requireNonNull(instrumentCode, "instrumentCode must not be null"))
                .toList();

        ensureUnique(normalizedInstrumentCodes);
        ensureSourcePlansExist(normalizedInstrumentCodes);

        port.replaceSelectedInstrumentCodes(normalizedInstrumentCodes);
    }

    private void ensureUnique(List<InstrumentCode> instrumentCodes) {
        Set<InstrumentCode> unique = new HashSet<>();

        for (InstrumentCode instrumentCode : instrumentCodes) {
            if (!unique.add(instrumentCode)) {
                throw new DuplicateQuoteMonitorInstrumentCodeException(instrumentCode);
            }
        }
    }

    private void ensureSourcePlansExist(List<InstrumentCode> instrumentCodes) {
        List<InstrumentCode> missing = port.findInstrumentCodesWithoutSourcePlan(instrumentCodes);

        if (!missing.isEmpty()) {
            throw new QuoteMonitorSourcePlanNotFoundException(missing);
        }
    }
}
