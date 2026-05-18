package com.alligator.market.domain.process.quotemonitor.instrument;

import com.alligator.market.domain.instrument.vo.InstrumentCode;
import com.alligator.market.domain.process.quotemonitor.instrument.exception.DuplicateQuoteMonitorInstrumentCodeException;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@SuppressWarnings("ClassCanBeRecord") // Domain aggregate state, not a DTO-style data carrier.
public final class QuoteMonitorInstrumentSelection {
    private final List<InstrumentCode> instrumentCodes;

    public QuoteMonitorInstrumentSelection(List<InstrumentCode> instrumentCodes) {
        this.instrumentCodes = copyAndValidate(
                Objects.requireNonNull(instrumentCodes, "instrumentCodes must not be null")
        );
    }

    public static QuoteMonitorInstrumentSelection empty() {
        return new QuoteMonitorInstrumentSelection(List.of());
    }

    public List<InstrumentCode> instrumentCodes() {
        return instrumentCodes;
    }

    public boolean contains(InstrumentCode instrumentCode) {
        Objects.requireNonNull(instrumentCode, "instrumentCode must not be null");

        return instrumentCodes.contains(instrumentCode);
    }

    public QuoteMonitorInstrumentSelection withInstrument(InstrumentCode instrumentCode) {
        Objects.requireNonNull(instrumentCode, "instrumentCode must not be null");

        if (contains(instrumentCode)) {
            return this;
        }

        List<InstrumentCode> updatedInstrumentCodes = new ArrayList<>(instrumentCodes);
        updatedInstrumentCodes.add(instrumentCode);

        return new QuoteMonitorInstrumentSelection(updatedInstrumentCodes);
    }

    public QuoteMonitorInstrumentSelection withoutInstrument(InstrumentCode instrumentCode) {
        Objects.requireNonNull(instrumentCode, "instrumentCode must not be null");

        if (!contains(instrumentCode)) {
            return this;
        }

        List<InstrumentCode> updatedInstrumentCodes = new ArrayList<>(instrumentCodes);
        updatedInstrumentCodes.remove(instrumentCode);

        return new QuoteMonitorInstrumentSelection(updatedInstrumentCodes);
    }

    private static List<InstrumentCode> copyAndValidate(List<InstrumentCode> instrumentCodes) {
        List<InstrumentCode> copied = new ArrayList<>(instrumentCodes.size());
        Set<InstrumentCode> unique = new HashSet<>();

        for (InstrumentCode instrumentCode : instrumentCodes) {
            InstrumentCode codeToCheck = Objects.requireNonNull(
                    instrumentCode,
                    "instrumentCode must not be null"
            );

            if (!unique.add(codeToCheck)) {
                throw new DuplicateQuoteMonitorInstrumentCodeException(codeToCheck);
            }

            copied.add(codeToCheck);
        }

        return List.copyOf(copied);
    }
}
