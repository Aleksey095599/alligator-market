package com.alligator.market.backend.process.quotemonitor.runtime;

import com.alligator.market.domain.instrument.vo.InstrumentCode;
import com.alligator.market.domain.marketdata.tick.level.source.SourceTick;
import com.alligator.market.domain.marketdata.tick.level.source.type.SourceLastPriceTick;
import com.alligator.market.domain.process.quotemonitor.capturer.QuoteMonitorCapturer;
import com.alligator.market.domain.process.quotemonitor.runtime.QuoteMonitorRuntimeProcess;
import com.alligator.market.domain.process.quotemonitor.runtime.state.QuoteMonitorRuntimeState;
import com.alligator.market.domain.process.quotemonitor.runtime.state.QuoteMonitorRuntimeStatus;
import com.alligator.market.domain.process.quotemonitor.runtime.state.instrument.QuoteMonitorInstrumentRuntimeState;
import com.alligator.market.domain.process.quotemonitor.runtime.state.instrument.QuoteMonitorInstrumentRuntimeStatus;
import com.alligator.market.domain.process.quotemonitor.runtime.start.QuoteMonitorRuntimeSourceAssignment;
import com.alligator.market.domain.process.quotemonitor.runtime.start.QuoteMonitorRuntimeStart;
import com.alligator.market.domain.process.quotemonitor.runtime.start.QuoteMonitorRuntimeStartResolution;
import com.alligator.market.domain.process.quotemonitor.marketdata.tick.captured.QuoteMonitorLastPriceCapturedTick;
import com.alligator.market.domain.process.quotemonitor.marketdata.tick.captured.registry.runtime.RuntimeQuoteMonitorLastPriceCapturedTickPublisher;
import com.alligator.market.domain.source.exception.HandlerNotFoundException;
import com.alligator.market.domain.source.exception.InstrumentNotSupportedByHandlerException;
import com.alligator.market.domain.source.vo.SourceCode;
import lombok.extern.slf4j.Slf4j;
import reactor.core.Disposable;
import reactor.core.publisher.Flux;

import java.time.Clock;
import java.time.Instant;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@Slf4j
public final class DefaultQuoteMonitorRuntimeProcess implements QuoteMonitorRuntimeProcess {
    private final QuoteMonitorCapturer capturer;
    private final QuoteMonitorRuntimeStart runtimeStart;
    private final RuntimeQuoteMonitorLastPriceCapturedTickPublisher lastPriceCapturedTickPublisher;
    private final Clock clock;
    private final Object lock = new Object();

    private QuoteMonitorRuntimeState state = QuoteMonitorRuntimeState.stopped();
    private List<Disposable> activeSubscriptions = List.of();

    public DefaultQuoteMonitorRuntimeProcess(
            QuoteMonitorCapturer capturer,
            QuoteMonitorRuntimeStart runtimeStart,
            RuntimeQuoteMonitorLastPriceCapturedTickPublisher lastPriceCapturedTickPublisher,
            Clock clock
    ) {
        this.capturer = Objects.requireNonNull(capturer, "capturer must not be null");
        this.runtimeStart = Objects.requireNonNull(
                runtimeStart,
                "runtimeStart must not be null"
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
            if (state.status() == QuoteMonitorRuntimeStatus.RUNNING) {
                return false;
            }

            lastPriceCapturedTickPublisher.clear();
            QuoteMonitorRuntimeStartResolution startResolution = runtimeStart.resolve();
            state = startResolution.initialState();
            logStartIssues(startResolution);

            QuoteMonitorStartedSourceAssignments startedAssignments =
                    subscribeToSourceAssignments(startResolution.sourceAssignments());
            activeSubscriptions = startedAssignments.subscriptions();
            List<InstrumentCode> monitoredInstrumentCodes = monitoredInstrumentCodes(startedAssignments.assignments());
            state = state.withMonitoredInstrumentCodes(monitoredInstrumentCodes);
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
            if (state.status() == QuoteMonitorRuntimeStatus.STOPPED) {
                return false;
            }

            List<InstrumentCode> monitoredInstrumentCodes = state.monitoredInstrumentCodes();
            disposeActiveSubscriptions();
            state = state
                    .withStatus(QuoteMonitorRuntimeStatus.STOPPED)
                    .withInstrumentStates(stoppedInstrumentStates(state.instrumentStates()));
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
            return state.status();
        }
    }

    @Override
    public QuoteMonitorRuntimeState state() {
        synchronized (lock) {
            return state;
        }
    }

    private static List<InstrumentCode> monitoredInstrumentCodes(List<QuoteMonitorRuntimeSourceAssignment> assignments) {
        Set<InstrumentCode> codes = new LinkedHashSet<>();

        for (QuoteMonitorRuntimeSourceAssignment assignment : assignments) {
            codes.add(assignment.instrument().instrumentCode());
        }

        return List.copyOf(codes);
    }

    private static List<String> instrumentCodeValues(List<InstrumentCode> instrumentCodes) {
        return instrumentCodes.stream()
                .map(InstrumentCode::value)
                .toList();
    }

    private void logStartIssues(QuoteMonitorRuntimeStartResolution startResolution) {
        for (QuoteMonitorInstrumentRuntimeState instrumentState : startResolution.initialState().issueInstrumentStates()) {
            log.warn(
                    "Quote Monitor start issue: instrumentCode={}, sourceCode={}, status={}, detail={}",
                    instrumentState.instrumentCode().value(),
                    instrumentState.sourceCode()
                            .map(SourceCode::value)
                            .orElse(null),
                    instrumentState.status(),
                    instrumentState.detail()
                            .orElse(null)
            );
        }
    }

    private QuoteMonitorStartedSourceAssignments subscribeToSourceAssignments(
            List<QuoteMonitorRuntimeSourceAssignment> assignments
    ) {
        List<QuoteMonitorRuntimeSourceAssignment> startedAssignments = new ArrayList<>(assignments.size());
        List<Disposable> subscriptions = new ArrayList<>(assignments.size());

        for (QuoteMonitorRuntimeSourceAssignment assignment : assignments) {
            try {
                Disposable subscription = Flux.from(assignment.source().streamSourceTicks(assignment.instrument()))
                        .subscribe(
                                tick -> onSourceTick(assignment, tick),
                                error -> onSourceTickStreamError(assignment, error)
                        );
                startedAssignments.add(assignment);
                subscriptions.add(subscription);
            } catch (RuntimeException ex) {
                state = state.withInstrumentState(runtimeIssue(
                        assignment.instrument().instrumentCode(),
                        assignment.sourceCode(),
                        streamStartFailureStatus(ex),
                        failureDetail(ex)
                ));
                log.warn(
                        "Failed to start Quote Monitor source tick stream: instrumentCode={}, sourceCode={}, reason={}",
                        assignment.instrument().instrumentCode().value(),
                        assignment.sourceCode().value(),
                        ex.getMessage(),
                        ex
                );
            }
        }

        return new QuoteMonitorStartedSourceAssignments(
                List.copyOf(startedAssignments),
                List.copyOf(subscriptions)
        );
    }

    private void onSourceTick(QuoteMonitorRuntimeSourceAssignment assignment, SourceTick tick) {
        Objects.requireNonNull(tick, "tick must not be null");

        synchronized (lock) {
            if (state.status() != QuoteMonitorRuntimeStatus.RUNNING) {
                return;
            }

            Instant receivedAt = clock.instant();
            state = state.withLastTickAt(receivedAt);
            boolean published = publishSourceTick(assignment, tick, receivedAt);
            if (published) {
                state = state.withInstrumentState(QuoteMonitorInstrumentRuntimeState.live(
                        assignment.instrument().instrumentCode(),
                        assignment.sourceCode()
                ));
            } else {
                state = state.withInstrumentState(runtimeIssue(
                        assignment.instrument().instrumentCode(),
                        assignment.sourceCode(),
                        QuoteMonitorInstrumentRuntimeStatus.UNSUPPORTED_SOURCE_TICK_TYPE,
                        "Unsupported source tick type: " + tick.sourceTickType()
                ));
            }
        }

        log.debug(
                "Quote Monitor source tick received: instrumentCode={}, sourceCode={}, sourceInstrumentCode={}",
                assignment.instrument().instrumentCode().value(),
                assignment.sourceCode().value(),
                tick.sourceInstrumentCode().value()
        );
    }

    private boolean publishSourceTick(
            QuoteMonitorRuntimeSourceAssignment assignment,
            SourceTick tick,
            Instant receivedAt
    ) {
        if (!(tick instanceof SourceLastPriceTick lastPriceTick)) {
            log.debug(
                    "Quote Monitor skipped unsupported source tick type: instrumentCode={}, sourceCode={}, type={}",
                    assignment.instrument().instrumentCode().value(),
                    assignment.sourceCode().value(),
                    tick.sourceTickType()
            );
            return false;
        }

        lastPriceCapturedTickPublisher.publish(new QuoteMonitorLastPriceCapturedTick(
                assignment.instrument().instrumentCode(),
                assignment.sourceCode(),
                lastPriceTick,
                receivedAt
        ));
        return true;
    }

    private void onSourceTickStreamError(QuoteMonitorRuntimeSourceAssignment assignment, Throwable error) {
        synchronized (lock) {
            if (state.status() == QuoteMonitorRuntimeStatus.RUNNING) {
                state = state.withInstrumentState(runtimeIssue(
                        assignment.instrument().instrumentCode(),
                        assignment.sourceCode(),
                        streamFailureStatus(error),
                        failureDetail(error)
                ));
            }
        }

        log.warn(
                "Quote Monitor source tick stream failed: instrumentCode={}, sourceCode={}, reason={}",
                assignment.instrument().instrumentCode().value(),
                assignment.sourceCode().value(),
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

    private record QuoteMonitorStartedSourceAssignments(
            List<QuoteMonitorRuntimeSourceAssignment> assignments,
            List<Disposable> subscriptions
    ) {
        private QuoteMonitorStartedSourceAssignments {
            assignments = List.copyOf(Objects.requireNonNull(assignments, "assignments must not be null"));
            subscriptions = List.copyOf(Objects.requireNonNull(subscriptions, "subscriptions must not be null"));
        }
    }
}
