package com.alligator.market.domain.process.quotemonitor.runtime.start;

import com.alligator.market.domain.instrument.Instrument;
import com.alligator.market.domain.source.MarketSource;
import com.alligator.market.domain.source.vo.SourceCode;
import com.alligator.market.domain.sourceplan.PrioritizedSourceCode;

import java.util.Objects;

public record QuoteMonitorRuntimeSourceAssignment(
        Instrument instrument,
        PrioritizedSourceCode prioritizedSourceCode,
        MarketSource source
) {
    public QuoteMonitorRuntimeSourceAssignment {
        Objects.requireNonNull(instrument, "instrument must not be null");
        Objects.requireNonNull(prioritizedSourceCode, "prioritizedSourceCode must not be null");
        Objects.requireNonNull(source, "source must not be null");
    }

    public SourceCode sourceCode() {
        return prioritizedSourceCode.sourceCode();
    }
}
