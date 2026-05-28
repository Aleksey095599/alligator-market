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
import com.alligator.market.domain.marketdata.tick.level.source.type.SourceTickType;
import com.alligator.market.domain.marketdata.tick.level.source.vo.SourceInstrumentCode;
import com.alligator.market.domain.process.quotemonitor.capturer.QuoteMonitorCapturer;
import com.alligator.market.domain.process.quotemonitor.instrument.QuoteMonitorInstrumentSelection;
import com.alligator.market.domain.process.quotemonitor.instrument.registry.runtime.RuntimeQuoteMonitorInstrumentSelectionRegistry;
import com.alligator.market.domain.process.quotemonitor.runtime.state.QuoteMonitorRuntimeStatus;
import com.alligator.market.domain.process.quotemonitor.runtime.state.instrument.QuoteMonitorInstrumentRuntimeState;
import com.alligator.market.domain.process.quotemonitor.runtime.state.instrument.QuoteMonitorInstrumentRuntimeStatus;
import com.alligator.market.domain.process.quotemonitor.runtime.start.RegistryBackedQuoteMonitorRuntimeStart;
import com.alligator.market.domain.process.quotemonitor.marketdata.tick.captured.QuoteMonitorLastPriceCapturedTick;
import com.alligator.market.domain.process.quotemonitor.marketdata.tick.captured.registry.runtime.RuntimeQuoteMonitorLastPriceCapturedTickPublisher;
import com.alligator.market.domain.source.MarketSource;
import com.alligator.market.domain.source.exception.HandlerNotFoundException;
import com.alligator.market.domain.source.exception.InstrumentNotSupportedByHandlerException;
import com.alligator.market.domain.source.passport.SourcePassport;
import com.alligator.market.domain.source.passport.vo.SourceDisplayName;
import com.alligator.market.domain.source.registry.RuntimeSourceRegistry;
import com.alligator.market.domain.source.vo.HandlerCode;
import com.alligator.market.domain.source.vo.SourceCode;
import com.alligator.market.domain.sourceplan.vo.PrioritizedSourceCode;
import com.alligator.market.domain.sourceplan.SourcePlan;
import com.alligator.market.domain.sourceplan.vo.SourcePlanKey;
import com.alligator.market.domain.sourceplan.registry.runtime.RuntimeSourcePlanRegistry;
import org.junit.jupiter.api.Test;
import org.reactivestreams.Publisher;
import reactor.core.publisher.Flux;

import java.math.BigDecimal;
import java.time.Clock;
import java.time.Instant;
import java.time.ZoneOffset;
import java.util.LinkedHashMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;

import static org.assertj.core.api.Assertions.assertThat;

class DefaultQuoteMonitorRuntimeProcessTest {
    private static final Instant START_TIME = Instant.parse("2026-05-18T08:00:00Z");

    @Test
    void startMovesProcessToRunningOnlyOnce() {
        DefaultQuoteMonitorRuntimeProcess process = process(
                new MutableRuntimeQuoteMonitorInstrumentSelectionRegistry(QuoteMonitorInstrumentSelection.empty()),
                new MutableRuntimeInstrumentRegistry(List.of()),
                new MutableRuntimeSourcePlanRegistry(List.of()),
                new MutableRuntimeSourceRegistry(List.of())
        );

        assertThat(process.start()).isTrue();
        assertThat(process.start()).isFalse();

        assertThat(process.status()).isEqualTo(QuoteMonitorRuntimeStatus.RUNNING);
        assertThat(process.state().lastTickAt()).isEmpty();
    }

    @Test
    void stopMovesProcessToStoppedOnlyOnce() {
        DefaultQuoteMonitorRuntimeProcess process = process(
                new MutableRuntimeQuoteMonitorInstrumentSelectionRegistry(QuoteMonitorInstrumentSelection.empty()),
                new MutableRuntimeInstrumentRegistry(List.of()),
                new MutableRuntimeSourcePlanRegistry(List.of()),
                new MutableRuntimeSourceRegistry(List.of())
        );

        process.start();

        assertThat(process.stop()).isTrue();
        assertThat(process.stop()).isFalse();

        assertThat(process.status()).isEqualTo(QuoteMonitorRuntimeStatus.STOPPED);
    }

    @Test
    void startUsesCurrentSelectionAndAvailableSourcePlans() {
        InstrumentCode firstCode = InstrumentCode.of("FOREX_SPOT_CNYRUB_TOM");
        InstrumentCode secondCode = InstrumentCode.of("FOREX_SPOT_USDRUB_TOM");
        TestInstrument firstInstrument = instrument(firstCode);
        TestInstrument secondInstrument = instrument(secondCode);
        RecordingMarketSource source = new RecordingMarketSource(SourceCode.of("PRIMARY_SOURCE"));
        CapturingRuntimeQuoteMonitorLastPriceCapturedTickPublisher tickPublisher =
                new CapturingRuntimeQuoteMonitorLastPriceCapturedTickPublisher();
        MutableRuntimeQuoteMonitorInstrumentSelectionRegistry selectionRegistry =
                new MutableRuntimeQuoteMonitorInstrumentSelectionRegistry(
                        new QuoteMonitorInstrumentSelection(List.of(firstCode, secondCode))
                );
        MutableRuntimeSourcePlanRegistry sourcePlanRegistry =
                new MutableRuntimeSourcePlanRegistry(List.of(plan(firstCode)));
        DefaultQuoteMonitorRuntimeProcess process = process(
                selectionRegistry,
                new MutableRuntimeInstrumentRegistry(List.of(firstInstrument, secondInstrument)),
                sourcePlanRegistry,
                new MutableRuntimeSourceRegistry(List.of(source)),
                tickPublisher
        );

        process.start();

        assertThat(process.state().monitoredInstrumentCodes()).containsExactly(firstCode);
        assertThat(instrumentState(process, firstCode).status())
                .isEqualTo(QuoteMonitorInstrumentRuntimeStatus.LIVE);
        assertThat(instrumentState(process, secondCode).status())
                .isEqualTo(QuoteMonitorInstrumentRuntimeStatus.RUNTIME_SOURCE_PLAN_NOT_FOUND);
        assertThat(process.state().lastTickAt()).contains(START_TIME);
        assertThat(source.streamedInstrumentCodes()).containsExactly(firstCode);
        assertThat(tickPublisher.clearCount()).isEqualTo(1);
        assertThat(tickPublisher.publishedTicks()).hasSize(1);
        assertThat(tickPublisher.publishedTicks().getFirst().instrumentCode()).isEqualTo(firstCode);
        assertThat(tickPublisher.publishedTicks().getFirst().sourceCode()).isEqualTo(source.code());
        assertThat(tickPublisher.publishedTicks().getFirst().sourceTick().lastPrice())
                .isEqualByComparingTo(BigDecimal.ONE);
    }

    @Test
    void startUsesHighestPriorityAvailableSourceCode() {
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
                        new PrioritizedSourceCode(SourceCode.of("BACKUP_SOURCE"), 1),
                        new PrioritizedSourceCode(SourceCode.of("PRIMARY_SOURCE"), 0)
                )));
        DefaultQuoteMonitorRuntimeProcess process = process(
                selectionRegistry,
                new MutableRuntimeInstrumentRegistry(List.of(instrument)),
                sourcePlanRegistry,
                new MutableRuntimeSourceRegistry(List.of(backupSource, primarySource))
        );

        process.start();

        assertThat(process.state().monitoredInstrumentCodes()).containsExactly(instrumentCode);
        assertThat(primarySource.streamedInstrumentCodes()).containsExactly(instrumentCode);
        assertThat(backupSource.streamedInstrumentCodes()).isEmpty();
    }

    @Test
    void startRecordsMissingRuntimeInstrumentIssue() {
        InstrumentCode instrumentCode = InstrumentCode.of("FOREX_SPOT_CNYRUB_TOM");
        DefaultQuoteMonitorRuntimeProcess process = process(
                new MutableRuntimeQuoteMonitorInstrumentSelectionRegistry(
                        new QuoteMonitorInstrumentSelection(List.of(instrumentCode))
                ),
                new MutableRuntimeInstrumentRegistry(List.of()),
                new MutableRuntimeSourcePlanRegistry(List.of()),
                new MutableRuntimeSourceRegistry(List.of())
        );

        process.start();

        assertThat(process.state().monitoredInstrumentCodes()).isEmpty();
        assertThat(onlyInstrumentState(process, instrumentCode).status())
                .isEqualTo(QuoteMonitorInstrumentRuntimeStatus.RUNTIME_INSTRUMENT_NOT_FOUND);
    }

    @Test
    void startRecordsMissingRuntimeSourcePlanIssue() {
        InstrumentCode instrumentCode = InstrumentCode.of("FOREX_SPOT_CNYRUB_TOM");
        DefaultQuoteMonitorRuntimeProcess process = process(
                new MutableRuntimeQuoteMonitorInstrumentSelectionRegistry(
                        new QuoteMonitorInstrumentSelection(List.of(instrumentCode))
                ),
                new MutableRuntimeInstrumentRegistry(List.of(instrument(instrumentCode))),
                new MutableRuntimeSourcePlanRegistry(List.of()),
                new MutableRuntimeSourceRegistry(List.of())
        );

        process.start();

        assertThat(process.state().monitoredInstrumentCodes()).isEmpty();
        assertThat(onlyInstrumentState(process, instrumentCode).status())
                .isEqualTo(QuoteMonitorInstrumentRuntimeStatus.RUNTIME_SOURCE_PLAN_NOT_FOUND);
    }

    @Test
    void startRecordsMissingRuntimeSourceIssue() {
        InstrumentCode instrumentCode = InstrumentCode.of("FOREX_SPOT_CNYRUB_TOM");
        DefaultQuoteMonitorRuntimeProcess process = process(
                new MutableRuntimeQuoteMonitorInstrumentSelectionRegistry(
                        new QuoteMonitorInstrumentSelection(List.of(instrumentCode))
                ),
                new MutableRuntimeInstrumentRegistry(List.of(instrument(instrumentCode))),
                new MutableRuntimeSourcePlanRegistry(List.of(plan(instrumentCode))),
                new MutableRuntimeSourceRegistry(List.of())
        );

        process.start();

        assertThat(process.state().monitoredInstrumentCodes()).isEmpty();
        assertThat(onlyInstrumentState(process, instrumentCode).status())
                .isEqualTo(QuoteMonitorInstrumentRuntimeStatus.RUNTIME_SOURCE_NOT_FOUND);
    }

    @Test
    void startRecordsHandlerNotFoundIssueWhenSourceCannotResolveHandler() {
        InstrumentCode instrumentCode = InstrumentCode.of("FOREX_SPOT_CNYRUB_TOM");
        ThrowingMarketSource source = new ThrowingMarketSource(
                SourceCode.of("PRIMARY_SOURCE"),
                code -> new HandlerNotFoundException(code, SourceCode.of("PRIMARY_SOURCE"))
        );
        DefaultQuoteMonitorRuntimeProcess process = process(
                new MutableRuntimeQuoteMonitorInstrumentSelectionRegistry(
                        new QuoteMonitorInstrumentSelection(List.of(instrumentCode))
                ),
                new MutableRuntimeInstrumentRegistry(List.of(instrument(instrumentCode))),
                new MutableRuntimeSourcePlanRegistry(List.of(plan(instrumentCode))),
                new MutableRuntimeSourceRegistry(List.of(source))
        );

        process.start();

        assertThat(process.state().monitoredInstrumentCodes()).isEmpty();
        assertThat(onlyInstrumentState(process, instrumentCode).status())
                .isEqualTo(QuoteMonitorInstrumentRuntimeStatus.HANDLER_NOT_FOUND);
    }

    @Test
    void startRecordsInstrumentNotSupportedIssueWhenHandlerRejectsInstrument() {
        InstrumentCode instrumentCode = InstrumentCode.of("FOREX_SPOT_CNYRUB_TOM");
        ThrowingMarketSource source = new ThrowingMarketSource(
                SourceCode.of("PRIMARY_SOURCE"),
                code -> InstrumentNotSupportedByHandlerException.instrumentCodeNotSupported(
                        code,
                        HandlerCode.of("TEST_HANDLER"),
                        Set.of(InstrumentCode.of("FOREX_SPOT_USDRUB_TOM"))
                )
        );
        DefaultQuoteMonitorRuntimeProcess process = process(
                new MutableRuntimeQuoteMonitorInstrumentSelectionRegistry(
                        new QuoteMonitorInstrumentSelection(List.of(instrumentCode))
                ),
                new MutableRuntimeInstrumentRegistry(List.of(instrument(instrumentCode))),
                new MutableRuntimeSourcePlanRegistry(List.of(plan(instrumentCode))),
                new MutableRuntimeSourceRegistry(List.of(source))
        );

        process.start();

        assertThat(process.state().monitoredInstrumentCodes()).isEmpty();
        assertThat(onlyInstrumentState(process, instrumentCode).status())
                .isEqualTo(QuoteMonitorInstrumentRuntimeStatus.INSTRUMENT_NOT_SUPPORTED_BY_HANDLER);
    }

    @Test
    void startRecordsStreamStartFailureIssueWhenSourceThrowsBeforePublisherIsCreated() {
        InstrumentCode instrumentCode = InstrumentCode.of("FOREX_SPOT_CNYRUB_TOM");
        ThrowingMarketSource source = new ThrowingMarketSource(
                SourceCode.of("PRIMARY_SOURCE"),
                ignored -> new IllegalStateException("Stream cannot be created")
        );
        DefaultQuoteMonitorRuntimeProcess process = process(
                new MutableRuntimeQuoteMonitorInstrumentSelectionRegistry(
                        new QuoteMonitorInstrumentSelection(List.of(instrumentCode))
                ),
                new MutableRuntimeInstrumentRegistry(List.of(instrument(instrumentCode))),
                new MutableRuntimeSourcePlanRegistry(List.of(plan(instrumentCode))),
                new MutableRuntimeSourceRegistry(List.of(source))
        );

        process.start();

        assertThat(process.state().monitoredInstrumentCodes()).isEmpty();
        QuoteMonitorInstrumentRuntimeState state = onlyInstrumentState(process, instrumentCode);
        assertThat(state.status()).isEqualTo(QuoteMonitorInstrumentRuntimeStatus.STREAM_START_FAILED);
        assertThat(state.detail()).contains("Stream cannot be created");
    }

    @Test
    void startRecordsStreamFailureIssueWhenPublisherFailsAfterSubscription() {
        InstrumentCode instrumentCode = InstrumentCode.of("FOREX_SPOT_CNYRUB_TOM");
        FailingPublisherMarketSource source = new FailingPublisherMarketSource(SourceCode.of("PRIMARY_SOURCE"));
        DefaultQuoteMonitorRuntimeProcess process = process(
                new MutableRuntimeQuoteMonitorInstrumentSelectionRegistry(
                        new QuoteMonitorInstrumentSelection(List.of(instrumentCode))
                ),
                new MutableRuntimeInstrumentRegistry(List.of(instrument(instrumentCode))),
                new MutableRuntimeSourcePlanRegistry(List.of(plan(instrumentCode))),
                new MutableRuntimeSourceRegistry(List.of(source))
        );

        process.start();

        assertThat(process.state().monitoredInstrumentCodes()).containsExactly(instrumentCode);
        assertThat(onlyInstrumentState(process, instrumentCode).status())
                .isEqualTo(QuoteMonitorInstrumentRuntimeStatus.STREAM_FAILED);
    }

    @Test
    void startRecordsUnsupportedTickTypeIssueWhenSourceEmitsNonLastPriceTick() {
        InstrumentCode instrumentCode = InstrumentCode.of("FOREX_SPOT_CNYRUB_TOM");
        UnsupportedTickMarketSource source = new UnsupportedTickMarketSource(SourceCode.of("PRIMARY_SOURCE"));
        DefaultQuoteMonitorRuntimeProcess process = process(
                new MutableRuntimeQuoteMonitorInstrumentSelectionRegistry(
                        new QuoteMonitorInstrumentSelection(List.of(instrumentCode))
                ),
                new MutableRuntimeInstrumentRegistry(List.of(instrument(instrumentCode))),
                new MutableRuntimeSourcePlanRegistry(List.of(plan(instrumentCode))),
                new MutableRuntimeSourceRegistry(List.of(source))
        );

        process.start();

        assertThat(process.state().monitoredInstrumentCodes()).containsExactly(instrumentCode);
        assertThat(onlyInstrumentState(process, instrumentCode).status())
                .isEqualTo(QuoteMonitorInstrumentRuntimeStatus.UNSUPPORTED_SOURCE_TICK_TYPE);
    }

    private static DefaultQuoteMonitorRuntimeProcess process(
            RuntimeQuoteMonitorInstrumentSelectionRegistry selectionRegistry,
            RuntimeInstrumentRegistry instrumentRegistry,
            RuntimeSourcePlanRegistry sourcePlanRegistry,
            RuntimeSourceRegistry sourceRegistry
    ) {
        return process(
                selectionRegistry,
                instrumentRegistry,
                sourcePlanRegistry,
                sourceRegistry,
                new CapturingRuntimeQuoteMonitorLastPriceCapturedTickPublisher()
        );
    }

    private static DefaultQuoteMonitorRuntimeProcess process(
            RuntimeQuoteMonitorInstrumentSelectionRegistry selectionRegistry,
            RuntimeInstrumentRegistry instrumentRegistry,
            RuntimeSourcePlanRegistry sourcePlanRegistry,
            RuntimeSourceRegistry sourceRegistry,
            RuntimeQuoteMonitorLastPriceCapturedTickPublisher tickPublisher
    ) {
        QuoteMonitorCapturer capturer = new QuoteMonitorCapturer();

        return new DefaultQuoteMonitorRuntimeProcess(
                capturer,
                new RegistryBackedQuoteMonitorRuntimeStart(
                        capturer,
                        selectionRegistry,
                        instrumentRegistry,
                        sourcePlanRegistry,
                        sourceRegistry
                ),
                tickPublisher,
                Clock.fixed(START_TIME, ZoneOffset.UTC)
        );
    }

    private static SourcePlan plan(InstrumentCode instrumentCode) {
        return plan(
                instrumentCode,
                new PrioritizedSourceCode(SourceCode.of("PRIMARY_SOURCE"), 0)
        );
    }

    private static SourcePlan plan(
            InstrumentCode instrumentCode,
            PrioritizedSourceCode... prioritizedSourceCodes
    ) {
        return new SourcePlan(
                QuoteMonitorCapturer.CAPTURER_CODE,
                instrumentCode,
                List.of(prioritizedSourceCodes)
        );
    }

    private static TestInstrument instrument(InstrumentCode instrumentCode) {
        return new TestInstrument(instrumentCode);
    }

    private static QuoteMonitorInstrumentRuntimeState onlyInstrumentState(
            DefaultQuoteMonitorRuntimeProcess process,
            InstrumentCode instrumentCode
    ) {
        assertThat(process.state().instrumentStates()).hasSize(1);

        return instrumentState(process, instrumentCode);
    }

    private static QuoteMonitorInstrumentRuntimeState instrumentState(
            DefaultQuoteMonitorRuntimeProcess process,
            InstrumentCode instrumentCode
    ) {
        return process.state()
                .instrumentState(instrumentCode)
                .orElseThrow();
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

        private MutableRuntimeSourcePlanRegistry(List<SourcePlan> availablePlans) {
            Map<SourcePlanKey, SourcePlan> updatedPlansByKey = new LinkedHashMap<>();

            for (SourcePlan plan : availablePlans) {
                updatedPlansByKey.put(plan.key(), plan);
            }

            this.plansByKey = Map.copyOf(updatedPlansByKey);
        }

        @Override
        public Optional<SourcePlan> findAvailableByKey(SourcePlanKey key) {
            return Optional.ofNullable(plansByKey.get(key));
        }

        @Override
        public List<SourcePlan> findAvailableByCapturerCode(CapturerCode capturerCode) {
            return plansByKey.values()
                    .stream()
                    .filter(plan -> plan.capturerCode().equals(capturerCode))
                    .toList();
        }

        @Override
        public Map<SourcePlanKey, SourcePlan> availablePlansByKey() {
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
        private final List<InstrumentCode> streamedInstrumentCodes = new ArrayList<>();

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

    private static final class ThrowingMarketSource implements MarketSource {
        private final SourceCode sourceCode;
        private final Function<InstrumentCode, RuntimeException> exceptionFactory;

        private ThrowingMarketSource(
                SourceCode sourceCode,
                Function<InstrumentCode, RuntimeException> exceptionFactory
        ) {
            this.sourceCode = sourceCode;
            this.exceptionFactory = exceptionFactory;
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
            throw exceptionFactory.apply(instrument.instrumentCode());
        }
    }

    private static final class FailingPublisherMarketSource implements MarketSource {
        private final SourceCode sourceCode;

        private FailingPublisherMarketSource(SourceCode sourceCode) {
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
            return Flux.error(new IllegalStateException("Stream failed"));
        }
    }

    private static final class UnsupportedTickMarketSource implements MarketSource {
        private final SourceCode sourceCode;

        private UnsupportedTickMarketSource(SourceCode sourceCode) {
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
            return Flux.just(new SourceTick() {
                @Override
                public SourceTickType sourceTickType() {
                    return SourceTickType.TOP_OF_BOOK_QUOTE;
                }

                @Override
                public SourceInstrumentCode sourceInstrumentCode() {
                    return SourceInstrumentCode.of(instrument.instrumentCode().value());
                }

                @Override
                public Instant sourceTickTime() {
                    return START_TIME;
                }
            });
        }
    }

    private static final class CapturingRuntimeQuoteMonitorLastPriceCapturedTickPublisher
            implements RuntimeQuoteMonitorLastPriceCapturedTickPublisher {
        private final List<QuoteMonitorLastPriceCapturedTick> publishedTicks = new ArrayList<>();
        private int clearCount;

        @Override
        public void publish(QuoteMonitorLastPriceCapturedTick tick) {
            publishedTicks.add(tick);
        }

        @Override
        public void clear() {
            clearCount++;
            publishedTicks.clear();
        }

        private List<QuoteMonitorLastPriceCapturedTick> publishedTicks() {
            return List.copyOf(publishedTicks);
        }

        private int clearCount() {
            return clearCount;
        }
    }
}
