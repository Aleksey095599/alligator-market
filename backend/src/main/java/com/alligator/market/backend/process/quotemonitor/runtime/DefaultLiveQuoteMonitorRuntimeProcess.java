package com.alligator.market.backend.process.quotemonitor.runtime;

import com.alligator.market.domain.instrument.Instrument;
import com.alligator.market.domain.instrument.registry.runtime.RuntimeInstrumentRegistry;
import com.alligator.market.domain.instrument.vo.InstrumentCode;
import com.alligator.market.domain.marketdata.tick.level.source.SourceTick;
import com.alligator.market.domain.marketdata.tick.level.source.type.SourceLastPriceTick;
import com.alligator.market.domain.process.quotemonitor.capturer.LiveQuoteMonitorCapturer;
import com.alligator.market.domain.process.quotemonitor.instrument.registry.runtime.RuntimeQuoteMonitorInstrumentSelectionRegistry;
import com.alligator.market.domain.process.quotemonitor.livequote.QuoteMonitorLiveQuote;
import com.alligator.market.domain.process.quotemonitor.livequote.registry.runtime.RuntimeQuoteMonitorLiveQuotePublisher;
import com.alligator.market.domain.process.quotemonitor.runtime.LiveQuoteMonitorInstrumentRuntimeState;
import com.alligator.market.domain.process.quotemonitor.runtime.LiveQuoteMonitorInstrumentRuntimeStatus;
import com.alligator.market.domain.process.quotemonitor.runtime.LiveQuoteMonitorRuntimeProcess;
import com.alligator.market.domain.process.quotemonitor.runtime.LiveQuoteMonitorRuntimeSnapshot;
import com.alligator.market.domain.process.quotemonitor.runtime.LiveQuoteMonitorRuntimeStatus;
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
public final class DefaultLiveQuoteMonitorRuntimeProcess implements LiveQuoteMonitorRuntimeProcess {
    private final LiveQuoteMonitorCapturer capturer;
    private final RuntimeQuoteMonitorInstrumentSelectionRegistry instrumentSelectionRegistry;
    private final RuntimeInstrumentRegistry instrumentRegistry;
    private final RuntimeSourcePlanRegistry sourcePlanRegistry;
    private final RuntimeSourceRegistry sourceRegistry;
    private final RuntimeQuoteMonitorLiveQuotePublisher liveQuotePublisher;
    private final Clock clock;
    private final Object lock = new Object();

    private LiveQuoteMonitorRuntimeSnapshot snapshot = LiveQuoteMonitorRuntimeSnapshot.stopped();
    private List<Disposable> activeSubscriptions = List.of();

    public DefaultLiveQuoteMonitorRuntimeProcess(
            LiveQuoteMonitorCapturer capturer,
            RuntimeQuoteMonitorInstrumentSelectionRegistry instrumentSelectionRegistry,
            RuntimeInstrumentRegistry instrumentRegistry,
            RuntimeSourcePlanRegistry sourcePlanRegistry,
            RuntimeSourceRegistry sourceRegistry,
            RuntimeQuoteMonitorLiveQuotePublisher liveQuotePublisher,
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
        this.liveQuotePublisher = Objects.requireNonNull(
                liveQuotePublisher,
                "liveQuotePublisher must not be null"
        );
        this.clock = Objects.requireNonNull(clock, "clock must not be null");
    }

    @Override
    public boolean start() {
        synchronized (lock) {
            if (snapshot.status() == LiveQuoteMonitorRuntimeStatus.RUNNING) {
                return false;
            }

            liveQuotePublisher.clear();
            LiveQuoteMonitorSourceStreamResolution resolution = resolveSourceStreams();
            List<LiveQuoteMonitorSourceStream> streams = resolution.streams();
            snapshot = runningSnapshot(
                    monitoredInstrumentCodes(streams),
                    resolution.instrumentStates()
            );
            LiveQuoteMonitorStartedSourceStreams startedStreams = subscribeToSourceStreams(streams);
            activeSubscriptions = startedStreams.subscriptions();
            List<InstrumentCode> monitoredInstrumentCodes = monitoredInstrumentCodes(startedStreams.streams());
            snapshot = snapshot.withMonitoredInstrumentCodes(monitoredInstrumentCodes);
            log.info(
                    "Live Quote Monitor started: capturerCode={}, monitoredInstrumentCount={}, monitoredInstrumentCodes={}",
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
            if (snapshot.status() == LiveQuoteMonitorRuntimeStatus.STOPPED) {
                return false;
            }

            List<InstrumentCode> monitoredInstrumentCodes = snapshot.monitoredInstrumentCodes();
            disposeActiveSubscriptions();
            snapshot = snapshot
                    .withStatus(LiveQuoteMonitorRuntimeStatus.STOPPED)
                    .withInstrumentStates(stoppedInstrumentStates(snapshot.instrumentStates()));
            log.info(
                    "Live Quote Monitor stopped: capturerCode={}, monitoredInstrumentCount={}, monitoredInstrumentCodes={}",
                    capturer.capturerCode().value(),
                    monitoredInstrumentCodes.size(),
                    instrumentCodeValues(monitoredInstrumentCodes)
            );
            return true;
        }
    }

    @Override
    public LiveQuoteMonitorRuntimeStatus status() {
        synchronized (lock) {
            return snapshot.status();
        }
    }

    @Override
    public LiveQuoteMonitorRuntimeSnapshot snapshot() {
        synchronized (lock) {
            return snapshot;
        }
    }

    private LiveQuoteMonitorRuntimeSnapshot runningSnapshot(
            List<InstrumentCode> monitoredInstrumentCodes,
            List<LiveQuoteMonitorInstrumentRuntimeState> instrumentStates
    ) {
        return new LiveQuoteMonitorRuntimeSnapshot(
                LiveQuoteMonitorRuntimeStatus.RUNNING,
                monitoredInstrumentCodes,
                null,
                instrumentStates
        );
    }

    private LiveQuoteMonitorSourceStreamResolution resolveSourceStreams() {
        List<LiveQuoteMonitorSourceStream> streams = new ArrayList<>();
        List<LiveQuoteMonitorInstrumentRuntimeState> instrumentStates = new ArrayList<>();

        for (InstrumentCode instrumentCode : instrumentSelectionRegistry.selectedInstrumentCodes()) {
            Optional<Instrument> instrument = resolveInstrument(instrumentCode);
            if (instrument.isEmpty()) {
                instrumentStates.add(runtimeIssue(
                        instrumentCode,
                        null,
                        LiveQuoteMonitorInstrumentRuntimeStatus.RUNTIME_INSTRUMENT_NOT_FOUND,
                        "Selected instrument is absent from runtime instrument registry"
                ));
                continue;
            }

            Optional<SourcePlan> sourcePlan = resolveSourcePlan(instrumentCode);
            if (sourcePlan.isEmpty()) {
                instrumentStates.add(runtimeIssue(
                        instrumentCode,
                        null,
                        LiveQuoteMonitorInstrumentRuntimeStatus.RUNTIME_SOURCE_PLAN_NOT_FOUND,
                        "Executable source plan is absent from runtime source plan registry"
                ));
                continue;
            }

            Optional<LiveQuoteMonitorSourceStream> stream = resolveSourceStream(
                    new LiveQuoteMonitorInstrumentPlan(instrument.get(), sourcePlan.get())
            );
            if (stream.isEmpty()) {
                instrumentStates.add(runtimeIssue(
                        instrumentCode,
                        null,
                        LiveQuoteMonitorInstrumentRuntimeStatus.RUNTIME_SOURCE_NOT_FOUND,
                        "Source plan has no entries available in runtime source registry"
                ));
                continue;
            }

            LiveQuoteMonitorSourceStream resolvedStream = stream.get();
            streams.add(resolvedStream);
            instrumentStates.add(LiveQuoteMonitorInstrumentRuntimeState.waitingForQuote(
                    instrumentCode,
                    resolvedStream.sourceCode()
            ));
        }

        return new LiveQuoteMonitorSourceStreamResolution(
                List.copyOf(streams),
                List.copyOf(instrumentStates)
        );
    }

    private Optional<Instrument> resolveInstrument(InstrumentCode instrumentCode) {
        Optional<Instrument> instrument = instrumentRegistry.findByCode(instrumentCode);
        if (instrument.isEmpty()) {
            log.warn(
                    "Live Quote Monitor selected instrument is absent from runtime instrument registry: instrumentCode={}",
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
                    "Live Quote Monitor selected instrument has no executable source plan: capturerCode={}, instrumentCode={}",
                    capturer.capturerCode().value(),
                    instrumentCode.value()
            );
        }

        return sourcePlan;
    }

    private Optional<LiveQuoteMonitorSourceStream> resolveSourceStream(
            LiveQuoteMonitorInstrumentPlan instrumentPlan
    ) {
        Map<SourceCode, MarketSource> sourcesByCode = sourceRegistry.sourcesByCode();

        return instrumentPlan.sourcePlan().entries()
                .stream()
                .sorted(Comparator.comparingInt(SourcePlanEntry::priority))
                .flatMap(entry -> {
                    MarketSource source = sourcesByCode.get(entry.sourceCode());
                    if (source == null) {
                        log.warn(
                                "Live Quote Monitor source plan entry is absent from runtime source registry: " +
                                        "instrumentCode={}, sourceCode={}",
                                instrumentPlan.instrument().instrumentCode().value(),
                                entry.sourceCode().value()
                        );
                        return java.util.stream.Stream.empty();
                    }

                    return java.util.stream.Stream.of(new LiveQuoteMonitorSourceStream(
                            instrumentPlan.instrument(),
                            entry.sourceCode(),
                            source
                    ));
                })
                .findFirst();
    }

    private List<InstrumentCode> monitoredInstrumentCodes(List<LiveQuoteMonitorSourceStream> streams) {
        Set<InstrumentCode> codes = new LinkedHashSet<>();

        for (LiveQuoteMonitorSourceStream stream : streams) {
            codes.add(stream.instrument().instrumentCode());
        }

        return List.copyOf(codes);
    }

    private static List<String> instrumentCodeValues(List<InstrumentCode> instrumentCodes) {
        return instrumentCodes.stream()
                .map(InstrumentCode::value)
                .toList();
    }

    private LiveQuoteMonitorStartedSourceStreams subscribeToSourceStreams(List<LiveQuoteMonitorSourceStream> streams) {
        List<LiveQuoteMonitorSourceStream> startedStreams = new ArrayList<>(streams.size());
        List<Disposable> subscriptions = new ArrayList<>(streams.size());

        for (LiveQuoteMonitorSourceStream stream : streams) {
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
                        "Failed to start Live Quote Monitor source stream: instrumentCode={}, sourceCode={}, reason={}",
                        stream.instrument().instrumentCode().value(),
                        stream.sourceCode().value(),
                        ex.getMessage(),
                        ex
                );
            }
        }

        return new LiveQuoteMonitorStartedSourceStreams(
                List.copyOf(startedStreams),
                List.copyOf(subscriptions)
        );
    }

    private void onSourceTick(LiveQuoteMonitorSourceStream stream, SourceTick tick) {
        Objects.requireNonNull(tick, "tick must not be null");

        synchronized (lock) {
            if (snapshot.status() != LiveQuoteMonitorRuntimeStatus.RUNNING) {
                return;
            }

            Instant receivedAt = clock.instant();
            snapshot = snapshot.withLastTickAt(receivedAt);
            boolean published = publishSourceTick(stream, tick, receivedAt);
            if (published) {
                snapshot = snapshot.withInstrumentState(LiveQuoteMonitorInstrumentRuntimeState.live(
                        stream.instrument().instrumentCode(),
                        stream.sourceCode()
                ));
            } else {
                snapshot = snapshot.withInstrumentState(runtimeIssue(
                        stream.instrument().instrumentCode(),
                        stream.sourceCode(),
                        LiveQuoteMonitorInstrumentRuntimeStatus.UNSUPPORTED_SOURCE_TICK_TYPE,
                        "Unsupported source tick type: " + tick.sourceTickType()
                ));
            }
        }

        log.debug(
                "Live Quote Monitor source tick received: instrumentCode={}, sourceCode={}, sourceInstrumentCode={}",
                stream.instrument().instrumentCode().value(),
                stream.sourceCode().value(),
                tick.sourceInstrumentCode().value()
        );
    }

    private boolean publishSourceTick(
            LiveQuoteMonitorSourceStream stream,
            SourceTick tick,
            Instant receivedAt
    ) {
        if (!(tick instanceof SourceLastPriceTick lastPriceTick)) {
            log.debug(
                    "Live Quote Monitor skipped unsupported source tick type: instrumentCode={}, sourceCode={}, type={}",
                    stream.instrument().instrumentCode().value(),
                    stream.sourceCode().value(),
                    tick.sourceTickType()
            );
            return false;
        }

        liveQuotePublisher.publish(new QuoteMonitorLiveQuote(
                stream.instrument().instrumentCode(),
                stream.sourceCode(),
                lastPriceTick.lastPrice(),
                lastPriceTick.sourceTickTime(),
                receivedAt
        ));
        return true;
    }

    private void onSourceStreamError(LiveQuoteMonitorSourceStream stream, Throwable error) {
        synchronized (lock) {
            if (snapshot.status() == LiveQuoteMonitorRuntimeStatus.RUNNING) {
                snapshot = snapshot.withInstrumentState(runtimeIssue(
                        stream.instrument().instrumentCode(),
                        stream.sourceCode(),
                        streamFailureStatus(error),
                        failureDetail(error)
                ));
            }
        }

        log.warn(
                "Live Quote Monitor source stream failed: instrumentCode={}, sourceCode={}, reason={}",
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

    private static List<LiveQuoteMonitorInstrumentRuntimeState> stoppedInstrumentStates(
            List<LiveQuoteMonitorInstrumentRuntimeState> instrumentStates
    ) {
        return instrumentStates.stream()
                .map(state -> LiveQuoteMonitorInstrumentRuntimeState.stopped(state.instrumentCode()))
                .toList();
    }

    private static LiveQuoteMonitorInstrumentRuntimeState runtimeIssue(
            InstrumentCode instrumentCode,
            SourceCode sourceCode,
            LiveQuoteMonitorInstrumentRuntimeStatus status,
            String detail
    ) {
        return LiveQuoteMonitorInstrumentRuntimeState.issue(
                instrumentCode,
                sourceCode,
                status,
                detail
        );
    }

    private static LiveQuoteMonitorInstrumentRuntimeStatus streamStartFailureStatus(Throwable error) {
        if (error instanceof HandlerNotFoundException) {
            return LiveQuoteMonitorInstrumentRuntimeStatus.HANDLER_NOT_FOUND;
        }
        if (error instanceof InstrumentNotSupportedByHandlerException) {
            return LiveQuoteMonitorInstrumentRuntimeStatus.INSTRUMENT_NOT_SUPPORTED_BY_HANDLER;
        }

        return LiveQuoteMonitorInstrumentRuntimeStatus.STREAM_START_FAILED;
    }

    private static LiveQuoteMonitorInstrumentRuntimeStatus streamFailureStatus(Throwable error) {
        if (error instanceof HandlerNotFoundException) {
            return LiveQuoteMonitorInstrumentRuntimeStatus.HANDLER_NOT_FOUND;
        }
        if (error instanceof InstrumentNotSupportedByHandlerException) {
            return LiveQuoteMonitorInstrumentRuntimeStatus.INSTRUMENT_NOT_SUPPORTED_BY_HANDLER;
        }

        return LiveQuoteMonitorInstrumentRuntimeStatus.STREAM_FAILED;
    }

    private static String failureDetail(Throwable error) {
        String message = error.getMessage();
        if (message == null || message.isBlank()) {
            return error.getClass().getSimpleName();
        }

        return message;
    }

    private record LiveQuoteMonitorSourceStreamResolution(
            List<LiveQuoteMonitorSourceStream> streams,
            List<LiveQuoteMonitorInstrumentRuntimeState> instrumentStates
    ) {
        private LiveQuoteMonitorSourceStreamResolution {
            streams = List.copyOf(Objects.requireNonNull(streams, "streams must not be null"));
            instrumentStates = List.copyOf(
                    Objects.requireNonNull(instrumentStates, "instrumentStates must not be null")
            );
        }
    }

    private record LiveQuoteMonitorInstrumentPlan(
            Instrument instrument,
            SourcePlan sourcePlan
    ) {
        private LiveQuoteMonitorInstrumentPlan {
            Objects.requireNonNull(instrument, "instrument must not be null");
            Objects.requireNonNull(sourcePlan, "sourcePlan must not be null");
        }
    }

    private record LiveQuoteMonitorSourceStream(
            Instrument instrument,
            SourceCode sourceCode,
            MarketSource source
    ) {
        private LiveQuoteMonitorSourceStream {
            Objects.requireNonNull(instrument, "instrument must not be null");
            Objects.requireNonNull(sourceCode, "sourceCode must not be null");
            Objects.requireNonNull(source, "source must not be null");
        }
    }

    private record LiveQuoteMonitorStartedSourceStreams(
            List<LiveQuoteMonitorSourceStream> streams,
            List<Disposable> subscriptions
    ) {
        private LiveQuoteMonitorStartedSourceStreams {
            streams = List.copyOf(Objects.requireNonNull(streams, "streams must not be null"));
            subscriptions = List.copyOf(Objects.requireNonNull(subscriptions, "subscriptions must not be null"));
        }
    }
}
