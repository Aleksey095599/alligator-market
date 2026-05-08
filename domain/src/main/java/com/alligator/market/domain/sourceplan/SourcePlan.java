package com.alligator.market.domain.sourceplan;

import com.alligator.market.domain.instrument.vo.InstrumentCode;
import com.alligator.market.domain.capturer.vo.MarketDataCapturerCode;
import com.alligator.market.domain.source.vo.MarketDataSourceCode;

import java.util.*;

/**
 * A source plan for one market data capturer and one instrument.
 */
@SuppressWarnings("ClassCanBeRecord") // Domain model, not a DTO-style data carrier.
public final class SourcePlan {

    private final MarketDataCapturerCode capturerCode;
    private final InstrumentCode instrumentCode;
    private final List<SourcePlanEntry> entries;

    /**
     * Creates a source plan.
     *
     * @param capturerCode   the market data capturer code
     * @param instrumentCode the instrument code
     * @param entries        the source plan entries
     */
    public SourcePlan(
            MarketDataCapturerCode capturerCode,
            InstrumentCode instrumentCode,
            List<SourcePlanEntry> entries
    ) {
        this.capturerCode = Objects.requireNonNull(capturerCode, "capturerCode must not be null");
        this.instrumentCode = Objects.requireNonNull(instrumentCode, "instrumentCode must not be null");
        this.entries = copyAndValidateEntries(Objects.requireNonNull(entries, "entries must not be null"));
    }

    /**
     * Validate entries and create a safe copy.
     */
    private static List<SourcePlanEntry> copyAndValidateEntries(
            List<SourcePlanEntry> entries
    ) {
        if (entries.isEmpty()) {
            throw new IllegalArgumentException("entries must not be empty");
        }

        List<SourcePlanEntry> entriesValidated = new ArrayList<>(entries.size());
        Set<MarketDataSourceCode> sourceCodes = new HashSet<>();
        Set<Integer> priorities = new HashSet<>();

        for (SourcePlanEntry entry : entries) {
            SourcePlanEntry entryToCheck = Objects.requireNonNull(entry, "entry must not be null");

            if (!sourceCodes.add(entryToCheck.sourceCode())) {
                throw new IllegalArgumentException(
                        "Source plan contains duplicate source code '" +
                                entryToCheck.sourceCode().value() + "'"
                );
            }

            if (!priorities.add(entryToCheck.priority())) {
                throw new IllegalArgumentException(
                        "Source plan contains duplicate priority '" +
                                entryToCheck.priority() + "'"
                );
            }

            entriesValidated.add(entryToCheck);
        }

        return List.copyOf(entriesValidated);
    }

    public MarketDataCapturerCode capturerCode() {
        return capturerCode;
    }

    public InstrumentCode instrumentCode() {
        return instrumentCode;
    }

    public List<SourcePlanEntry> entries() {
        return entries;
    }
}
