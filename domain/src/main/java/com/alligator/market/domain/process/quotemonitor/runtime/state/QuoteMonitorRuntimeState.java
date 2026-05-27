package com.alligator.market.domain.process.quotemonitor.runtime.state;

import com.alligator.market.domain.instrument.vo.InstrumentCode;
import com.alligator.market.domain.process.quotemonitor.runtime.state.instrument.QuoteMonitorInstrumentRuntimeState;

import java.time.Instant;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

public final class QuoteMonitorRuntimeState {
    private final QuoteMonitorRuntimeStatus status;
    private final List<InstrumentCode> monitoredInstrumentCodes;
    private final Instant lastTickAt;
    private final List<QuoteMonitorInstrumentRuntimeState> instrumentStates;

    public QuoteMonitorRuntimeState(
            QuoteMonitorRuntimeStatus status,
            List<InstrumentCode> monitoredInstrumentCodes,
            Instant lastTickAt
    ) {
        this(
                status,
                monitoredInstrumentCodes,
                lastTickAt,
                List.of()
        );
    }

    public QuoteMonitorRuntimeState(
            QuoteMonitorRuntimeStatus status,
            List<InstrumentCode> monitoredInstrumentCodes,
            Instant lastTickAt,
            List<QuoteMonitorInstrumentRuntimeState> instrumentStates
    ) {
        this.status = Objects.requireNonNull(status, "status must not be null");
        this.monitoredInstrumentCodes = List.copyOf(
                Objects.requireNonNull(monitoredInstrumentCodes, "monitoredInstrumentCodes must not be null")
        );
        this.lastTickAt = lastTickAt;
        this.instrumentStates = copyAndValidateInstrumentStates(instrumentStates);
    }

    public static QuoteMonitorRuntimeState stopped() {
        return new QuoteMonitorRuntimeState(
                QuoteMonitorRuntimeStatus.STOPPED,
                List.of(),
                null
        );
    }

    public QuoteMonitorRuntimeStatus status() {
        return status;
    }

    public List<InstrumentCode> monitoredInstrumentCodes() {
        return monitoredInstrumentCodes;
    }

    public Optional<Instant> lastTickAt() {
        return Optional.ofNullable(lastTickAt);
    }

    public List<QuoteMonitorInstrumentRuntimeState> instrumentStates() {
        return instrumentStates;
    }

    public Optional<QuoteMonitorInstrumentRuntimeState> instrumentState(InstrumentCode instrumentCode) {
        Objects.requireNonNull(instrumentCode, "instrumentCode must not be null");

        return instrumentStates.stream()
                .filter(state -> state.instrumentCode().equals(instrumentCode))
                .findFirst();
    }

    public List<QuoteMonitorInstrumentRuntimeState> issueInstrumentStates() {
        return instrumentStates.stream()
                .filter(QuoteMonitorInstrumentRuntimeState::isIssue)
                .toList();
    }

    public boolean hasInstrumentIssues() {
        return instrumentStates.stream()
                .anyMatch(QuoteMonitorInstrumentRuntimeState::isIssue);
    }

    public QuoteMonitorRuntimeState withStatus(QuoteMonitorRuntimeStatus status) {
        return new QuoteMonitorRuntimeState(
                status,
                monitoredInstrumentCodes,
                lastTickAt,
                instrumentStates
        );
    }

    public QuoteMonitorRuntimeState withLastTickAt(Instant lastTickAt) {
        return new QuoteMonitorRuntimeState(
                status,
                monitoredInstrumentCodes,
                Objects.requireNonNull(lastTickAt, "lastTickAt must not be null"),
                instrumentStates
        );
    }

    public QuoteMonitorRuntimeState withMonitoredInstrumentCodes(
            List<InstrumentCode> monitoredInstrumentCodes
    ) {
        return new QuoteMonitorRuntimeState(
                status,
                monitoredInstrumentCodes,
                lastTickAt,
                instrumentStates
        );
    }

    public QuoteMonitorRuntimeState withInstrumentStates(
            List<QuoteMonitorInstrumentRuntimeState> instrumentStates
    ) {
        return new QuoteMonitorRuntimeState(
                status,
                monitoredInstrumentCodes,
                lastTickAt,
                instrumentStates
        );
    }

    public QuoteMonitorRuntimeState withInstrumentState(
            QuoteMonitorInstrumentRuntimeState instrumentState
    ) {
        Objects.requireNonNull(instrumentState, "instrumentState must not be null");

        Map<InstrumentCode, QuoteMonitorInstrumentRuntimeState> statesByInstrumentCode =
                new LinkedHashMap<>();
        for (QuoteMonitorInstrumentRuntimeState currentState : instrumentStates) {
            statesByInstrumentCode.put(currentState.instrumentCode(), currentState);
        }
        statesByInstrumentCode.put(instrumentState.instrumentCode(), instrumentState);

        return withInstrumentStates(statesByInstrumentCode.values()
                .stream()
                .toList()
        );
    }

    private static List<QuoteMonitorInstrumentRuntimeState> copyAndValidateInstrumentStates(
            List<QuoteMonitorInstrumentRuntimeState> instrumentStates
    ) {
        Objects.requireNonNull(instrumentStates, "instrumentStates must not be null");

        Map<InstrumentCode, QuoteMonitorInstrumentRuntimeState> statesByInstrumentCode =
                new LinkedHashMap<>();
        for (QuoteMonitorInstrumentRuntimeState state : instrumentStates) {
            QuoteMonitorInstrumentRuntimeState stateToCheck =
                    Objects.requireNonNull(state, "instrumentState must not be null");
            QuoteMonitorInstrumentRuntimeState previous = statesByInstrumentCode.put(
                    stateToCheck.instrumentCode(),
                    stateToCheck
            );
            if (previous != null) {
                throw new IllegalArgumentException(
                        "Duplicate Quote Monitor runtime state detected for instrument code '" +
                                stateToCheck.instrumentCode().value() + "'"
                );
            }
        }

        return List.copyOf(statesByInstrumentCode.values());
    }
}
