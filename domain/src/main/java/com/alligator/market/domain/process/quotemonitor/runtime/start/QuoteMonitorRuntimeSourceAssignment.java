package com.alligator.market.domain.process.quotemonitor.runtime.start;

import com.alligator.market.domain.instrument.Instrument;
import com.alligator.market.domain.source.MarketSource;
import com.alligator.market.domain.source.vo.SourceCode;
import com.alligator.market.domain.sourceplan.SourcePlanEntry;

import java.util.Objects;

public record QuoteMonitorRuntimeSourceAssignment(
        Instrument instrument,
        SourcePlanEntry sourcePlanEntry,
        MarketSource source
) {
    public QuoteMonitorRuntimeSourceAssignment {
        Objects.requireNonNull(instrument, "instrument must not be null");
        Objects.requireNonNull(sourcePlanEntry, "sourcePlanEntry must not be null");
        Objects.requireNonNull(source, "source must not be null");
    }

    public SourceCode sourceCode() {
        return sourcePlanEntry.sourceCode();
    }
}
