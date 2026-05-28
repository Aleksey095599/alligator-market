package com.alligator.market.domain.process.quotemonitor.runtime.start;

import com.alligator.market.domain.instrument.Instrument;
import com.alligator.market.domain.instrument.registry.runtime.RuntimeInstrumentRegistry;
import com.alligator.market.domain.instrument.vo.InstrumentCode;
import com.alligator.market.domain.process.quotemonitor.capturer.QuoteMonitorCapturer;
import com.alligator.market.domain.process.quotemonitor.instrument.registry.runtime.RuntimeQuoteMonitorInstrumentSelectionRegistry;
import com.alligator.market.domain.source.MarketSource;
import com.alligator.market.domain.source.registry.RuntimeSourceRegistry;
import com.alligator.market.domain.source.vo.SourceCode;
import com.alligator.market.domain.sourceplan.vo.PrioritizedSourceCode;
import com.alligator.market.domain.sourceplan.SourcePlan;
import com.alligator.market.domain.sourceplan.vo.SourcePlanKey;
import com.alligator.market.domain.sourceplan.registry.runtime.RuntimeSourcePlanRegistry;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

public final class RegistryBackedQuoteMonitorRuntimeStart implements QuoteMonitorRuntimeStart {
    private final QuoteMonitorCapturer capturer;
    private final RuntimeQuoteMonitorInstrumentSelectionRegistry instrumentSelectionRegistry;
    private final RuntimeInstrumentRegistry instrumentRegistry;
    private final RuntimeSourcePlanRegistry sourcePlanRegistry;
    private final RuntimeSourceRegistry sourceRegistry;

    public RegistryBackedQuoteMonitorRuntimeStart(
            QuoteMonitorCapturer capturer,
            RuntimeQuoteMonitorInstrumentSelectionRegistry instrumentSelectionRegistry,
            RuntimeInstrumentRegistry instrumentRegistry,
            RuntimeSourcePlanRegistry sourcePlanRegistry,
            RuntimeSourceRegistry sourceRegistry
    ) {
        this.capturer = Objects.requireNonNull(capturer, "capturer must not be null");
        this.instrumentSelectionRegistry = Objects.requireNonNull(
                instrumentSelectionRegistry,
                "instrumentSelectionRegistry must not be null"
        );
        this.instrumentRegistry = Objects.requireNonNull(
                instrumentRegistry,
                "instrumentRegistry must not be null"
        );
        this.sourcePlanRegistry = Objects.requireNonNull(
                sourcePlanRegistry,
                "sourcePlanRegistry must not be null"
        );
        this.sourceRegistry = Objects.requireNonNull(
                sourceRegistry,
                "sourceRegistry must not be null"
        );
    }

    @Override
    public List<InstrumentCode> selectedInstrumentCodes() {
        return instrumentSelectionRegistry.selectedInstrumentCodes();
    }

    @Override
    public Optional<Instrument> findInstrumentInRuntimeRegistry(InstrumentCode instrumentCode) {
        return instrumentRegistry.findByCode(instrumentCode);
    }

    @Override
    public Optional<SourcePlan> findAvailableSourcePlan(InstrumentCode instrumentCode) {
        return sourcePlanRegistry.findAvailableByKey(new SourcePlanKey(
                capturer.capturerCode(),
                instrumentCode
        ));
    }

    @Override
    public Optional<QuoteMonitorRuntimeSourceAssignment> resolveSourceAssignmentFromPlan(
            Instrument instrument,
            SourcePlan sourcePlan
    ) {
        Map<SourceCode, MarketSource> sourcesByCode = sourceRegistry.sourcesByCode();

        return sourcePlan.prioritizedSourceCodes()
                .stream()
                .sorted(Comparator.comparingInt(PrioritizedSourceCode::priority))
                .flatMap(prioritizedSourceCode -> {
                    MarketSource source = sourcesByCode.get(prioritizedSourceCode.sourceCode());
                    if (source == null) {
                        return java.util.stream.Stream.empty();
                    }

                    return java.util.stream.Stream.of(new QuoteMonitorRuntimeSourceAssignment(
                            instrument,
                            prioritizedSourceCode,
                            source
                    ));
                })
                .findFirst();
    }
}
