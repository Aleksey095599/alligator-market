package com.alligator.market.domain.sourceplan;

import com.alligator.market.domain.instrument.vo.InstrumentCode;
import com.alligator.market.domain.marketdata.capturer.vo.MarketDataCapturerCode;
import com.alligator.market.domain.source.vo.MarketDataSourceCode;

import java.util.*;

/**
 * A source plan for one market data capturer and one instrument.
 */
@SuppressWarnings("ClassCanBeRecord") // Domain model, not a DTO-style data carrier.
public final class MarketDataSourcePlan {

    private final MarketDataCapturerCode capturerCode;
    private final InstrumentCode instrumentCode;
    private final List<MarketDataSourcePlanEntry> entries;

    /**
     * Creates a market data source plan.
     *
     * @param capturerCode   the market data capturer code
     * @param instrumentCode the instrument code
     * @param entries        the source plan entries
     */
    public MarketDataSourcePlan(
            MarketDataCapturerCode capturerCode,
            InstrumentCode instrumentCode,
            List<MarketDataSourcePlanEntry> entries
    ) {
        this.capturerCode = Objects.requireNonNull(capturerCode, "capturerCode must not be null");
        this.instrumentCode = Objects.requireNonNull(instrumentCode, "instrumentCode must not be null");
        this.entries = copyAndValidateEntries(Objects.requireNonNull(entries, "entries must not be null"));
    }

    /**
     * Validate entries and create a safe copy.
     */
    private static List<MarketDataSourcePlanEntry> copyAndValidateEntries(
            List<MarketDataSourcePlanEntry> entries
    ) {
        if (entries.isEmpty()) {
            throw new IllegalArgumentException("entries must not be empty");
        }

        List<MarketDataSourcePlanEntry> entriesValidated = new ArrayList<>(entries.size());
        Set<MarketDataSourceCode> sourceCodes = new HashSet<>();
        Set<Integer> priorities = new HashSet<>();

        for (MarketDataSourcePlanEntry entry : entries) {
            MarketDataSourcePlanEntry entryToCheck = Objects.requireNonNull(entry, "entry must not be null");

            if (!sourceCodes.add(entryToCheck.sourceCode())) {
                throw new IllegalArgumentException(
                        "Market data source plan contains duplicate source code '" +
                                entryToCheck.sourceCode().value() + "'"
                );
            }

            if (!priorities.add(entryToCheck.priority())) {
                throw new IllegalArgumentException(
                        "Market data source plan contains duplicate priority '" +
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

    public List<MarketDataSourcePlanEntry> entries() {
        return entries;
    }
}
