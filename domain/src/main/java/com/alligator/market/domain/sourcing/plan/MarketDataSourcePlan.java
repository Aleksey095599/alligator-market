package com.alligator.market.domain.sourcing.plan;

import com.alligator.market.domain.instrument.vo.InstrumentCode;
import com.alligator.market.domain.marketdata.capture.process.vo.MarketDataCaptureProcessCode;
import com.alligator.market.domain.provider.vo.ProviderCode;

import java.util.*;

/**
 * A market data source plan for an instrument in a special capture process.
 */
@SuppressWarnings("ClassCanBeRecord")
public final class MarketDataSourcePlan {

    private final MarketDataCaptureProcessCode captureProcessCode;
    private final InstrumentCode instrumentCode;
    private final List<MarketDataSourcePlanEntry> entries;

    /**
     * Creates a market data source plan.
     *
     * @param captureProcessCode the market data capture process code
     * @param instrumentCode     the instrument code
     * @param entries            the source plan entries
     */
    public MarketDataSourcePlan(
            MarketDataCaptureProcessCode captureProcessCode,
            InstrumentCode instrumentCode,
            List<MarketDataSourcePlanEntry> entries
    ) {
        this.captureProcessCode = Objects.requireNonNull(
                captureProcessCode,
                "captureProcessCode must not be null"
        );
        this.instrumentCode = Objects.requireNonNull(instrumentCode, "instrumentCode must not be null");
        Objects.requireNonNull(entries, "entries must not be null");

        this.entries = copyAndValidateEntries(entries);
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
        Set<ProviderCode> providerCodes = new HashSet<>();
        Set<Integer> priorities = new HashSet<>();

        for (MarketDataSourcePlanEntry entry : entries) {
            MarketDataSourcePlanEntry entryToCheck = Objects.requireNonNull(entry, "entry must not be null");

            if (!providerCodes.add(entryToCheck.providerCode())) {
                throw new IllegalArgumentException(
                        "Market data source plan contains duplicate provider code '" +
                                entryToCheck.providerCode().value() + "'"
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

    public MarketDataCaptureProcessCode captureProcessCode() {
        return captureProcessCode;
    }

    public InstrumentCode instrumentCode() {
        return instrumentCode;
    }

    public List<MarketDataSourcePlanEntry> entries() {
        return entries;
    }
}
