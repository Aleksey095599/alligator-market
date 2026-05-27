package com.alligator.market.backend.process.quotemonitor.runtime;

import com.alligator.market.domain.instrument.Instrument;
import com.alligator.market.domain.instrument.registry.runtime.RuntimeInstrumentRegistry;
import com.alligator.market.domain.instrument.vo.InstrumentCode;
import com.alligator.market.domain.marketdata.tick.level.source.SourceTick;
import com.alligator.market.domain.marketdata.tick.level.source.type.SourceLastPriceTick;
import com.alligator.market.domain.process.quotemonitor.capturer.QuoteMonitorCapturer;
import com.alligator.market.domain.process.quotemonitor.instrument.registry.runtime.RuntimeQuoteMonitorInstrumentSelectionRegistry;
import com.alligator.market.domain.process.quotemonitor.runtime.QuoteMonitorRuntimeProcess;
import com.alligator.market.domain.process.quotemonitor.runtime.QuoteMonitorRuntimeSnapshot;
import com.alligator.market.domain.process.quotemonitor.runtime.QuoteMonitorRuntimeStatus;
import com.alligator.market.domain.process.quotemonitor.runtime.instrument.QuoteMonitorInstrumentRuntimeState;
import com.alligator.market.domain.process.quotemonitor.runtime.instrument.QuoteMonitorInstrumentRuntimeStatus;
import com.alligator.market.domain.process.quotemonitor.marketdata.tick.QuoteMonitorLastPriceCapturedTick;
import com.alligator.market.domain.process.quotemonitor.marketdata.tick.registry.runtime.RuntimeQuoteMonitorLastPriceCapturedTickPublisher;
import com.alligator.market.domain.source.MarketSource;
import com.alligator.market.domain.source.exception.HandlerNotFoundException;
import com.alligator.market.domain.source.exception.InstrumentNotSupportedByHandlerException;
import com.alligator.market.domain.source.registry.RuntimeSourceRegistry;
import com.alligator.market.domain.source.vo.SourceCode;
import com.alligator.market.domain.sourceplan.SourcePlan;
import com.alligator.market.domain.sourceplan.SourcePlanEntry;
import com.alligator.market.domain.sourceplan.SourcePlanKey;
import com.alligator.market.domain.sourceplan.registry.runtime.RuntimeSourcePlanRegistry;
import lombok.extern.slf4j.Slf4j;
import reactor.core.Disposable;
import reactor.core.publisher.Flux;

import java.time.Clock;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

@Slf4j
public final class DefaultQuoteMonitorRuntimeProcess implements QuoteMonitorRuntimeProcess {
    private final QuoteMonitorCapturer capturer;
    private final RuntimeQuoteMonitorInstrumentSelectionRegistry instrumentSelectionRegistry;
    private final RuntimeInstrumentRegistry instrumentRegistry;
    private final RuntimeSourcePlanRegistry sourcePlanRegistry;
    private final RuntimeSourceRegistry sourceRegistry;
    private final RuntimeQuoteMonitorLastPriceCapturedTickPublisher lastPriceCapturedTickPublisher;
    private final Clock clock;
    private final Object lock = new Object();

    private QuoteMonitorRuntimeSnapshot snapshot = QuoteMonitorRuntimeSnapshot.stopped();
    private List<Disposable> activeSubscriptions = List.of();

    public DefaultQuoteMonitorRuntimeProcess(
            QuoteMonitorCapturer capturer,
            RuntimeQuoteMonitorInstrumentSelectionRegistry instrumentSelectionRegistry,
            RuntimeInstrumentRegistry instrumentRegistry,
            RuntimeSourcePlanRegistry sourcePlanRegistry,
            RuntimeSourceRegistry sourceRegistry,
            RuntimeQuoteMonitorLastPriceCapturedTickPublisher lastPriceCapturedTickPublisher,
            Clock clock
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
        this.lastPriceCapturedTickPublisher = Objects.requireNonNull(
                lastPriceCapturedTickPublisher,
                "lastPriceCapturedTickPublisher must not be null"
        );
        this.clock = Objects.requireNonNull(clock, "clock must not be null");
    }

    @Override
    public boolean start() {
        synchronized (lock) {
            if (snapshot.status() == QuoteMonitorRuntimeStatus.RUNNING) {
                return false;
            }

            lastPriceCapturedTickPublisher.clear();
            QuoteMonitorSourceStreamResolution resolution = resolveSourceStreams();
            List<QuoteMonitorSourceStream> streams = resolution.streams();
            snapshot = runningSnapshot(
                    monitoredInstrumentCodes(streams),
                    resolution.instrumentStates()
            );
            QuoteMonitorStartedSourceStreams startedStreams = subscribeToSourceStreams(streams);
            activeSubscriptions = startedStreams.subscriptions();
            List<InstrumentCode> monitoredInstrumentCodes = monitoredInstrumentCodes(startedStreams.streams());
            snapshot = snapshot.withMonitoredInstrumentCodes(monitoredInstrumentCodes);
            log.info(
                    "Quote Monitor started: capturerCode={}, monitoredInstrumentCount={}, monitoredInstrumentCodes={}",
                    capturer.capturerCode().value(),
                    monitoredInstrumentCodes.size(),
                    instrumentCodeValues(monitoredInstrumentCodes)
            );
            return true;
        }
    }

    @Override
    public boolean stop() {
        synchronized (lock) {
            if (snapshot.status() == QuoteMonitorRuntimeStatus.STOPPED) {
                return false;
            }

            List<InstrumentCode> monitoredInstrumentCodes = snapshot.monitoredInstrumentCodes();
            disposeActiveSubscriptions();
            snapshot = snapshot
                    .withStatus(QuoteMonitorRuntimeStatus.STOPPED)
                    .withInstrumentStates(stoppedInstrumentStates(snapshot.instrumentStates()));
            log.info(
                    "Quote Monitor stopped: capturerCode={}, monitoredInstrumentCount={}, monitoredInstrumentCodes={}",
                    capturer.capturerCode().value(),
                    monitoredInstrumentCodes.size(),
                    instrumentCodeValues(monitoredInstrumentCodes)
            );
            return true;
        }
    }

    @Override
    public QuoteMonitorRuntimeStatus status() {
        synchronized (lock) {
            return snapshot.status();
        }
    }

    @Override
    public QuoteMonitorRuntimeSnapshot snapshot() {
        synchronized (lock) {
            return snapshot;
        }
    }

    private QuoteMonitorRuntimeSnapshot runningSnapshot(
            List<InstrumentCode> monitoredInstrumentCodes,
            List<QuoteMonitorInstrumentRuntimeState> instrumentStates
    ) {
        return new QuoteMonitorRuntimeSnapshot(
                QuoteMonitorRuntimeStatus.RUNNING,
                monitoredInstrumentCodes,
                null,
                instrumentStates
        );
    }

    private QuoteMonitorSourceStreamResolution resolveSourceStreams() {
        List<QuoteMonitorSourceStream> streams = new ArrayList<>();
        List<QuoteMonitorInstrumentRuntimeState> instrumentStates = new ArrayList<>();

        for (InstrumentCode instrumentCode : instrumentSelectionRegistry.selectedInstrumentCodes()) {
            Optional<Instrument> instrument = resolveInstrument(instrumentCode);
            if (instrument.isEmpty()) {
                instrumentStates.add(runtimeIssue(
                        instrumentCode,
                        null,
                        QuoteMonitorInstrumentRuntimeStatus.RUNTIME_INSTRUMENT_NOT_FOUND,
                        "Selected instrument is absent from runtime instrument registry"
                ));
                continue;
            }

            Optional<SourcePlan> sourcePlan = resolveSourcePlan(instrumentCode);
            if (sourcePlan.isEmpty()) {
                instrumentStates.add(runtimeIssue(
                        instrumentCode,
                        null,
                        QuoteMonitorInstrumentRuntimeStatus.RUNTIME_SOURCE_PLAN_NOT_FOUND,
                        "Executable source plan is absent from runtime source plan registry"
                ));
                continue;
            }

            Optional<QuoteMonitorSourceStream> stream = resolveSourceStream(
                    new QuoteMonitorInstrumentPlan(instrument.get(), sourcePlan.get())
            );
            if (stream.isEmpty()) {
                instrumentStates.add(runtimeIssue(
                        instrumentCode,
                        null,
                        QuoteMonitorInstrumentRuntimeStatus.RUNTIME_SOURCE_NOT_FOUND,
                        "Source plan has no entries available in runtime source registry"
                ));
                continue;
            }

            QuoteMonitorSourceStream resolvedStream = stream.get();
            streams.add(resolvedStream);
            instrumentStates.add(QuoteMonitorInstrumentRuntimeState.waitingForQuote(
                    instrumentCode,
                    resolvedStream.sourceCode()
            ));
        }

        return new QuoteMonitorSourceStreamResolution(
                List.copyOf(streams),
                List.copyOf(instrumentStates)
        );
    }

    private Optional<Instrument> resolveInstrument(InstrumentCode instrumentCode) {
        Optional<Instrument> instrument = instrumentRegistry.findByCode(instrumentCode);
        if (instrument.isEmpty()) {
            log.warn(
                    "Quote Monitor selected instrument is absent from runtime instrument registry: instrumentCode={}",
                    instrumentCode.value()
            );
        }

        return instrument;
    }

    private Optional<SourcePlan> resolveSourcePlan(InstrumentCode instrumentCode) {
        SourcePlanKey key = new SourcePlanKey(
                capturer.capturerCode(),
                instrumentCode
        );

        Optional<SourcePlan> sourcePlan = sourcePlanRegistry.findExecutableByKey(key);
        if (sourcePlan.isEmpty()) {
            log.warn(
                    "Quote Monitor selected instrument has no executable source plan: capturerCode={}, instrumentCode={}",
                    capturer.capturerCode().value(),
                    instrumentCode.value()
            );
        }

        return sourcePlan;
    }

    private Optional<QuoteMonitorSourceStream> resolveSourceStream(
            QuoteMonitorInstrumentPlan instrumentPlan
    ) {
        Map<SourceCode, MarketSource> sourcesByCode = sourceRegistry.sourcesByCode();

        return instrumentPlan.sourcePlan().entries()
                .stream()
                .sorted(Comparator.comparingInt(SourcePlanEntry::priority))
                .flatMap(entry -> {
                    MarketSource source = sourcesByCode.get(entry.sourceCode());
                    if (source == null) {
                        log.warn(
                                "Quote Monitor source plan entry is absent from runtime source registry: " +
                                        "instrumentCode={}, sourceCode={}",
                                instrumentPlan.instrument().instrumentCode().value(),
                                entry.sourceCode().value()
                        );
                        return java.util.stream.Stream.empty();
                    }

                    return java.util.stream.Stream.of(new QuoteMonitorSourceStream(
                            instrumentPlan.instrument(),
                            entry.sourceCode(),
                            source
                    ));
                })
                .findFirst();
    }

    private List<InstrumentCode> monitoredInstrumentCodes(List<QuoteMonitorSourceStream> streams) {
        Set<InstrumentCode> codes = new LinkedHashSet<>();

        for (QuoteMonitorSourceStream stream : streams) {
            codes.add(stream.instrument().instrumentCode());
        }

        return List.copyOf(codes);
    }

    private static List<String> instrumentCodeValues(List<InstrumentCode> instrumentCodes) {
        return instrumentCodes.stream()
                .map(InstrumentCode::value)
                .toList();
    }

    private QuoteMonitorStartedSourceStreams subscribeToSourceStreams(List<QuoteMonitorSourceStream> streams) {
        List<QuoteMonitorSourceStream> startedStreams = new ArrayList<>(streams.size());
        List<Disposable> subscriptions = new ArrayList<>(streams.size());

        for (QuoteMonitorSourceStream stream : streams) {
            try {
                Disposable subscription = Flux.from(stream.source().streamSourceTicks(stream.instrument()))
                        .subscribe(
                                tick -> onSourceTick(stream, tick),
                                error -> onSourceStreamError(stream, error)
                        );
                startedStreams.add(stream);
                subscriptions.add(subscription);
            } catch (RuntimeException ex) {
                snapshot = snapshot.withInstrumentState(runtimeIssue(
                        stream.instrument().instrumentCode(),
                        stream.sourceCode(),
                        streamStartFailureStatus(ex),
                        failureDetail(ex)
                ));
                log.warn(
                        "Failed to start Quote Monitor source stream: instrumentCode={}, sourceCode={}, reason={}",
                        stream.instrument().instrumentCode().value(),
                        stream.sourceCode().value(),
                        ex.getMessage(),
                        ex
                );
            }
        }

        return new QuoteMonitorStartedSourceStreams(
                List.copyOf(startedStreams),
                List.copyOf(subscriptions)
        );
    }

    private void onSourceTick(QuoteMonitorSourceStream stream, SourceTick tick) {
        Objects.requireNonNull(tick, "tick must not be null");

        synchronized (lock) {
            if (snapshot.status() != QuoteMonitorRuntimeStatus.RUNNING) {
                return;
            }

            Instant receivedAt = clock.instant();
            snapshot = snapshot.withLastTickAt(receivedAt);
            boolean published = publishSourceTick(stream, tick, receivedAt);
            if (published) {
                snapshot = snapshot.withInstrumentState(QuoteMonitorInstrumentRuntimeState.live(
                        stream.instrument().instrumentCode(),
                        stream.sourceCode()
                ));
            } else {
                snapshot = snapshot.withInstrumentState(runtimeIssue(
                        stream.instrument().instrumentCode(),
                        stream.sourceCode(),
                        QuoteMonitorInstrumentRuntimeStatus.UNSUPPORTED_SOURCE_TICK_TYPE,
                        "Unsupported source tick type: " + tick.sourceTickType()
                ));
            }
        }

        log.debug(
                "Quote Monitor source tick received: instrumentCode={}, sourceCode={}, sourceInstrumentCode={}",
                stream.instrument().instrumentCode().value(),
                stream.sourceCode().value(),
                tick.sourceInstrumentCode().value()
        );
    }

    private boolean publishSourceTick(
            QuoteMonitorSourceStream stream,
            SourceTick tick,
            Instant receivedAt
    ) {
        if (!(tick instanceof SourceLastPriceTick lastPriceTick)) {
            log.debug(
                    "Quote Monitor skipped unsupported source tick type: instrumentCode={}, sourceCode={}, type={}",
                    stream.instrument().instrumentCode().value(),
                    stream.sourceCode().value(),
                    tick.sourceTickType()
            );
            return false;
        }

        lastPriceCapturedTickPublisher.publish(new QuoteMonitorLastPriceCapturedTick(
                stream.instrument().instrumentCode(),
                stream.sourceCode(),
                lastPriceTick,
                receivedAt
        ));
        return true;
    }

    private void onSourceStreamError(QuoteMonitorSourceStream stream, Throwable error) {
        synchronized (lock) {
            if (snapshot.status() == QuoteMonitorRuntimeStatus.RUNNING) {
                snapshot = snapshot.withInstrumentState(runtimeIssue(
                        stream.instrument().instrumentCode(),
                        stream.sourceCode(),
                        streamFailureStatus(error),
                        failureDetail(error)
                ));
            }
        }

        log.warn(
                "Quote Monitor source stream failed: instrumentCode={}, sourceCode={}, reason={}",
                stream.instrument().instrumentCode().value(),
                stream.sourceCode().value(),
                error.getMessage(),
                error
        );
    }

    private void disposeActiveSubscriptions() {
        for (Disposable subscription : activeSubscriptions) {
            subscription.dispose();
        }

        activeSubscriptions = List.of();
    }

    private static List<QuoteMonitorInstrumentRuntimeState> stoppedInstrumentStates(
            List<QuoteMonitorInstrumentRuntimeState> instrumentStates
    ) {
        return instrumentStates.stream()
                .map(state -> QuoteMonitorInstrumentRuntimeState.stopped(state.instrumentCode()))
                .toList();
    }

    private static QuoteMonitorInstrumentRuntimeState runtimeIssue(
            InstrumentCode instrumentCode,
            SourceCode sourceCode,
            QuoteMonitorInstrumentRuntimeStatus status,
            String detail
    ) {
        return QuoteMonitorInstrumentRuntimeState.issue(
                instrumentCode,
                sourceCode,
                status,
                detail
        );
    }

    private static QuoteMonitorInstrumentRuntimeStatus streamStartFailureStatus(Throwable error) {
        if (error instanceof HandlerNotFoundException) {
            return QuoteMonitorInstrumentRuntimeStatus.HANDLER_NOT_FOUND;
        }
        if (error instanceof InstrumentNotSupportedByHandlerException) {
            return QuoteMonitorInstrumentRuntimeStatus.INSTRUMENT_NOT_SUPPORTED_BY_HANDLER;
        }

        return QuoteMonitorInstrumentRuntimeStatus.STREAM_START_FAILED;
    }

    private static QuoteMonitorInstrumentRuntimeStatus streamFailureStatus(Throwable error) {
        if (error instanceof HandlerNotFoundException) {
            return QuoteMonitorInstrumentRuntimeStatus.HANDLER_NOT_FOUND;
        }
        if (error instanceof InstrumentNotSupportedByHandlerException) {
            return QuoteMonitorInstrumentRuntimeStatus.INSTRUMENT_NOT_SUPPORTED_BY_HANDLER;
        }

        return QuoteMonitorInstrumentRuntimeStatus.STREAM_FAILED;
    }

    private static String failureDetail(Throwable error) {
        String message = error.getMessage();
        if (message == null || message.isBlank()) {
            return error.getClass().getSimpleName();
        }

        return message;
    }

    private record QuoteMonitorSourceStreamResolution(
            List<QuoteMonitorSourceStream> streams,
            List<QuoteMonitorInstrumentRuntimeState> instrumentStates
    ) {
        private QuoteMonitorSourceStreamResolution {
            streams = List.copyOf(Objects.requireNonNull(streams, "streams must not be null"));
            instrumentStates = List.copyOf(
                    Objects.requireNonNull(instrumentStates, "instrumentStates must not be null")
            );
        }
    }

    private record QuoteMonitorInstrumentPlan(
            Instrument instrument,
            SourcePlan sourcePlan
    ) {
        private QuoteMonitorInstrumentPlan {
            Objects.requireNonNull(instrument, "instrument must not be null");
            Objects.requireNonNull(sourcePlan, "sourcePlan must not be null");
        }
    }

    private record QuoteMonitorSourceStream(
            Instrument instrument,
            SourceCode sourceCode,
            MarketSource source
    ) {
        private QuoteMonitorSourceStream {
            Objects.requireNonNull(instrument, "instrument must not be null");
            Objects.requireNonNull(sourceCode, "sourceCode must not be null");
            Objects.requireNonNull(source, "source must not be null");
        }
    }

    private record QuoteMonitorStartedSourceStreams(
            List<QuoteMonitorSourceStream> streams,
            List<Disposable> subscriptions
    ) {
        private QuoteMonitorStartedSourceStreams {
            streams = List.copyOf(Objects.requireNonNull(streams, "streams must not be null"));
            subscriptions = List.copyOf(Objects.requireNonNull(subscriptions, "subscriptions must not be null"));
        }
    }
}
