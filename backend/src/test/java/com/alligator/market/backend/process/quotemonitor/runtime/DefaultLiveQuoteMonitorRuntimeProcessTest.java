package com.alligator.market.backend.process.quotemonitor.runtime;

import com.alligator.market.domain.capturer.vo.CapturerCode;
import com.alligator.market.domain.instrument.Asset;
import com.alligator.market.domain.instrument.Instrument;
import com.alligator.market.domain.instrument.Product;
import com.alligator.market.domain.instrument.registry.runtime.RuntimeInstrumentRegistry;
import com.alligator.market.domain.instrument.vo.InstrumentCode;
import com.alligator.market.domain.instrument.vo.InstrumentSymbol;
import com.alligator.market.domain.marketdata.tick.level.source.SourceTick;
import com.alligator.market.domain.marketdata.tick.level.source.type.SourceLastPriceTick;
import com.alligator.market.domain.marketdata.tick.level.source.vo.SourceInstrumentCode;
import com.alligator.market.domain.process.quotemonitor.capturer.LiveQuoteMonitorCapturer;
import com.alligator.market.domain.process.quotemonitor.instrument.QuoteMonitorInstrumentSelection;
import com.alligator.market.domain.process.quotemonitor.instrument.registry.runtime.RuntimeQuoteMonitorInstrumentSelectionRegistry;
import com.alligator.market.domain.process.quotemonitor.runtime.LiveQuoteMonitorRuntimeStatus;
import com.alligator.market.domain.source.MarketSource;
import com.alligator.market.domain.source.passport.SourcePassport;
import com.alligator.market.domain.source.passport.vo.SourceDisplayName;
import com.alligator.market.domain.source.registry.RuntimeSourceRegistry;
import com.alligator.market.domain.source.vo.SourceCode;
import com.alligator.market.domain.sourceplan.SourcePlan;
import com.alligator.market.domain.sourceplan.SourcePlanEntry;
import com.alligator.market.domain.sourceplan.SourcePlanKey;
import com.alligator.market.domain.sourceplan.registry.runtime.RuntimeSourcePlanRegistry;
import org.junit.jupiter.api.Test;
import org.reactivestreams.Publisher;
import reactor.core.publisher.Flux;

import java.math.BigDecimal;
import java.time.Clock;
import java.time.Instant;
import java.time.ZoneOffset;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

class DefaultLiveQuoteMonitorRuntimeProcessTest {
    private static final Instant START_TIME = Instant.parse("2026-05-18T08:00:00Z");

    @Test
    void startMovesProcessToRunningOnlyOnce() {
        DefaultLiveQuoteMonitorRuntimeProcess process = process(
                new MutableRuntimeQuoteMonitorInstrumentSelectionRegistry(QuoteMonitorInstrumentSelection.empty()),
                new MutableRuntimeInstrumentRegistry(List.of()),
                new MutableRuntimeSourcePlanRegistry(List.of()),
                new MutableRuntimeSourceRegistry(List.of())
        );

        assertThat(process.start()).isTrue();
        assertThat(process.start()).isFalse();

        assertThat(process.status()).isEqualTo(LiveQuoteMonitorRuntimeStatus.RUNNING);
        assertThat(process.snapshot().lastTickAt()).isEmpty();
    }

    @Test
    void stopMovesProcessToStoppedOnlyOnce() {
        DefaultLiveQuoteMonitorRuntimeProcess process = process(
                new MutableRuntimeQuoteMonitorInstrumentSelectionRegistry(QuoteMonitorInstrumentSelection.empty()),
                new MutableRuntimeInstrumentRegistry(List.of()),
                new MutableRuntimeSourcePlanRegistry(List.of()),
                new MutableRuntimeSourceRegistry(List.of())
        );

        process.start();

        assertThat(process.stop()).isTrue();
        assertThat(process.stop()).isFalse();

        assertThat(process.status()).isEqualTo(LiveQuoteMonitorRuntimeStatus.STOPPED);
    }

    @Test
    void startUsesCurrentSelectionAndExecutableSourcePlans() {
        InstrumentCode firstCode = InstrumentCode.of("FOREX_SPOT_CNYRUB_TOM");
        InstrumentCode secondCode = InstrumentCode.of("FOREX_SPOT_USDRUB_TOM");
        TestInstrument firstInstrument = instrument(firstCode);
        TestInstrument secondInstrument = instrument(secondCode);
        RecordingMarketSource source = new RecordingMarketSource(SourceCode.of("PRIMARY_SOURCE"));
        MutableRuntimeQuoteMonitorInstrumentSelectionRegistry selectionRegistry =
                new MutableRuntimeQuoteMonitorInstrumentSelectionRegistry(
                        new QuoteMonitorInstrumentSelection(List.of(firstCode, secondCode))
                );
        MutableRuntimeSourcePlanRegistry sourcePlanRegistry =
                new MutableRuntimeSourcePlanRegistry(List.of(plan(firstCode)));
        DefaultLiveQuoteMonitorRuntimeProcess process = process(
                selectionRegistry,
                new MutableRuntimeInstrumentRegistry(List.of(firstInstrument, secondInstrument)),
                sourcePlanRegistry,
                new MutableRuntimeSourceRegistry(List.of(source))
        );

        process.start();

        assertThat(process.snapshot().monitoredInstrumentCodes()).containsExactly(firstCode);
        assertThat(process.snapshot().lastTickAt()).contains(START_TIME);
        assertThat(source.streamedInstrumentCodes()).containsExactly(firstCode);
    }

    @Test
    void startUsesHighestPriorityAvailableSourcePlanEntry() {
        InstrumentCode instrumentCode = InstrumentCode.of("FOREX_SPOT_CNYRUB_TOM");
        TestInstrument instrument = instrument(instrumentCode);
        RecordingMarketSource primarySource = new RecordingMarketSource(SourceCode.of("PRIMARY_SOURCE"));
        RecordingMarketSource backupSource = new RecordingMarketSource(SourceCode.of("BACKUP_SOURCE"));
        MutableRuntimeQuoteMonitorInstrumentSelectionRegistry selectionRegistry =
                new MutableRuntimeQuoteMonitorInstrumentSelectionRegistry(
                        new QuoteMonitorInstrumentSelection(List.of(instrumentCode))
                );
        MutableRuntimeSourcePlanRegistry sourcePlanRegistry =
                new MutableRuntimeSourcePlanRegistry(List.of(plan(
                        instrumentCode,
                        new SourcePlanEntry(SourceCode.of("BACKUP_SOURCE"), 1),
                        new SourcePlanEntry(SourceCode.of("PRIMARY_SOURCE"), 0)
                )));
        DefaultLiveQuoteMonitorRuntimeProcess process = process(
                selectionRegistry,
                new MutableRuntimeInstrumentRegistry(List.of(instrument)),
                sourcePlanRegistry,
                new MutableRuntimeSourceRegistry(List.of(backupSource, primarySource))
        );

        process.start();

        assertThat(process.snapshot().monitoredInstrumentCodes()).containsExactly(instrumentCode);
        assertThat(primarySource.streamedInstrumentCodes()).containsExactly(instrumentCode);
        assertThat(backupSource.streamedInstrumentCodes()).isEmpty();
    }

    private static DefaultLiveQuoteMonitorRuntimeProcess process(
            RuntimeQuoteMonitorInstrumentSelectionRegistry selectionRegistry,
            RuntimeInstrumentRegistry instrumentRegistry,
            RuntimeSourcePlanRegistry sourcePlanRegistry,
            RuntimeSourceRegistry sourceRegistry
    ) {
        return new DefaultLiveQuoteMonitorRuntimeProcess(
                new LiveQuoteMonitorCapturer(),
                selectionRegistry,
                instrumentRegistry,
                sourcePlanRegistry,
                sourceRegistry,
                Clock.fixed(START_TIME, ZoneOffset.UTC)
        );
    }

    private static SourcePlan plan(InstrumentCode instrumentCode) {
        return plan(
                instrumentCode,
                new SourcePlanEntry(SourceCode.of("PRIMARY_SOURCE"), 0)
        );
    }

    private static SourcePlan plan(
            InstrumentCode instrumentCode,
            SourcePlanEntry... entries
    ) {
        return new SourcePlan(
                LiveQuoteMonitorCapturer.CAPTURER_CODE,
                instrumentCode,
                List.of(entries)
        );
    }

    private static TestInstrument instrument(InstrumentCode instrumentCode) {
        return new TestInstrument(instrumentCode);
    }

    private record TestInstrument(InstrumentCode instrumentCode) implements Instrument {

        @Override
        public InstrumentSymbol instrumentSymbol() {
            return InstrumentSymbol.of(instrumentCode.value());
        }

        @Override
        public Asset asset() {
            return Asset.FOREX;
        }

        @Override
        public Product product() {
            return Product.SPOT;
        }
    }

    private static final class MutableRuntimeQuoteMonitorInstrumentSelectionRegistry
            implements RuntimeQuoteMonitorInstrumentSelectionRegistry {
        private QuoteMonitorInstrumentSelection selection;

        private MutableRuntimeQuoteMonitorInstrumentSelectionRegistry(
                QuoteMonitorInstrumentSelection selection
        ) {
            this.selection = selection;
        }

        @Override
        public QuoteMonitorInstrumentSelection currentSelection() {
            return selection;
        }

        @Override
        public List<InstrumentCode> selectedInstrumentCodes() {
            return selection.instrumentCodes();
        }

        @Override
        public boolean isSelected(InstrumentCode instrumentCode) {
            return selection.contains(instrumentCode);
        }
    }

    private static final class MutableRuntimeSourcePlanRegistry implements RuntimeSourcePlanRegistry {
        private final Map<SourcePlanKey, SourcePlan> plansByKey;

        private MutableRuntimeSourcePlanRegistry(List<SourcePlan> executablePlans) {
            Map<SourcePlanKey, SourcePlan> updatedPlansByKey = new LinkedHashMap<>();

            for (SourcePlan plan : executablePlans) {
                updatedPlansByKey.put(plan.key(), plan);
            }

            this.plansByKey = Map.copyOf(updatedPlansByKey);
        }

        @Override
        public Optional<SourcePlan> findExecutableByKey(SourcePlanKey key) {
            return Optional.ofNullable(plansByKey.get(key));
        }

        @Override
        public List<SourcePlan> findExecutableByCapturerCode(CapturerCode capturerCode) {
            return plansByKey.values()
                    .stream()
                    .filter(plan -> plan.capturerCode().equals(capturerCode))
                    .toList();
        }

        @Override
        public Map<SourcePlanKey, SourcePlan> executablePlansByKey() {
            return plansByKey;
        }
    }

    private static final class MutableRuntimeInstrumentRegistry implements RuntimeInstrumentRegistry {
        private final Map<InstrumentCode, Instrument> instrumentsByCode;

        private MutableRuntimeInstrumentRegistry(List<? extends Instrument> instruments) {
            Map<InstrumentCode, Instrument> updatedInstrumentsByCode = new LinkedHashMap<>();

            for (Instrument instrument : instruments) {
                updatedInstrumentsByCode.put(instrument.instrumentCode(), instrument);
            }

            this.instrumentsByCode = Map.copyOf(updatedInstrumentsByCode);
        }

        @Override
        public Optional<Instrument> findByCode(InstrumentCode instrumentCode) {
            return Optional.ofNullable(instrumentsByCode.get(instrumentCode));
        }

        @Override
        public Map<InstrumentCode, Instrument> instrumentsByCode() {
            return instrumentsByCode;
        }
    }

    private static final class MutableRuntimeSourceRegistry implements RuntimeSourceRegistry {
        private final Map<SourceCode, MarketSource> sourcesByCode;

        private MutableRuntimeSourceRegistry(List<? extends MarketSource> sources) {
            Map<SourceCode, MarketSource> updatedSourcesByCode = new LinkedHashMap<>();

            for (MarketSource source : sources) {
                updatedSourcesByCode.put(source.code(), source);
            }

            this.sourcesByCode = Map.copyOf(updatedSourcesByCode);
        }

        @Override
        public Map<SourceCode, MarketSource> sourcesByCode() {
            return sourcesByCode;
        }
    }

    private static final class RecordingMarketSource implements MarketSource {
        private final SourceCode sourceCode;
        private final List<InstrumentCode> streamedInstrumentCodes = new java.util.ArrayList<>();

        private RecordingMarketSource(SourceCode sourceCode) {
            this.sourceCode = sourceCode;
        }

        @Override
        public SourceCode code() {
            return sourceCode;
        }

        @Override
        public SourcePassport passport() {
            return new SourcePassport(SourceDisplayName.of("Test Source"));
        }

        @Override
        public <I extends Instrument> Publisher<SourceTick> streamSourceTicks(I instrument) {
            streamedInstrumentCodes.add(instrument.instrumentCode());

            return Flux.just(new SourceLastPriceTick(
                    SourceInstrumentCode.of(instrument.instrumentCode().value()),
                    BigDecimal.ONE,
                    START_TIME
            ));
        }

        private List<InstrumentCode> streamedInstrumentCodes() {
            return List.copyOf(streamedInstrumentCodes);
        }
    }
}
