package com.alligator.market.domain.process.quotemonitor.runtime;

import com.alligator.market.domain.instrument.vo.InstrumentCode;

import java.time.Instant;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

public final class LiveQuoteMonitorRuntimeSnapshot {
    private final LiveQuoteMonitorRuntimeStatus status;
    private final List<InstrumentCode> monitoredInstrumentCodes;
    private final Instant lastTickAt;
    private final List<LiveQuoteMonitorInstrumentRuntimeState> instrumentStates;

    public LiveQuoteMonitorRuntimeSnapshot(
            LiveQuoteMonitorRuntimeStatus status,
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

    public LiveQuoteMonitorRuntimeSnapshot(
            LiveQuoteMonitorRuntimeStatus status,
            List<InstrumentCode> monitoredInstrumentCodes,
            Instant lastTickAt,
            List<LiveQuoteMonitorInstrumentRuntimeState> instrumentStates
    ) {
        this.status = Objects.requireNonNull(status, "status must not be null");
        this.monitoredInstrumentCodes = List.copyOf(
                Objects.requireNonNull(monitoredInstrumentCodes, "monitoredInstrumentCodes must not be null")
        );
        this.lastTickAt = lastTickAt;
        this.instrumentStates = copyAndValidateInstrumentStates(instrumentStates);
    }

    public static LiveQuoteMonitorRuntimeSnapshot stopped() {
        return new LiveQuoteMonitorRuntimeSnapshot(
                LiveQuoteMonitorRuntimeStatus.STOPPED,
                List.of(),
                null
        );
    }

    public LiveQuoteMonitorRuntimeStatus status() {
        return status;
    }

    public List<InstrumentCode> monitoredInstrumentCodes() {
        return monitoredInstrumentCodes;
    }

    public Optional<Instant> lastTickAt() {
        return Optional.ofNullable(lastTickAt);
    }

    public List<LiveQuoteMonitorInstrumentRuntimeState> instrumentStates() {
        return instrumentStates;
    }

    public LiveQuoteMonitorRuntimeSnapshot withStatus(LiveQuoteMonitorRuntimeStatus status) {
        return new LiveQuoteMonitorRuntimeSnapshot(
                status,
                monitoredInstrumentCodes,
                lastTickAt,
                instrumentStates
        );
    }

    public LiveQuoteMonitorRuntimeSnapshot withLastTickAt(Instant lastTickAt) {
        return new LiveQuoteMonitorRuntimeSnapshot(
                status,
                monitoredInstrumentCodes,
                Objects.requireNonNull(lastTickAt, "lastTickAt must not be null"),
                instrumentStates
        );
    }

    public LiveQuoteMonitorRuntimeSnapshot withMonitoredInstrumentCodes(
            List<InstrumentCode> monitoredInstrumentCodes
    ) {
        return new LiveQuoteMonitorRuntimeSnapshot(
                status,
                monitoredInstrumentCodes,
                lastTickAt,
                instrumentStates
        );
    }

    public LiveQuoteMonitorRuntimeSnapshot withInstrumentStates(
            List<LiveQuoteMonitorInstrumentRuntimeState> instrumentStates
    ) {
        return new LiveQuoteMonitorRuntimeSnapshot(
                status,
                monitoredInstrumentCodes,
                lastTickAt,
                instrumentStates
        );
    }

    public LiveQuoteMonitorRuntimeSnapshot withInstrumentState(
            LiveQuoteMonitorInstrumentRuntimeState instrumentState
    ) {
        Objects.requireNonNull(instrumentState, "instrumentState must not be null");

        Map<InstrumentCode, LiveQuoteMonitorInstrumentRuntimeState> statesByInstrumentCode =
                new LinkedHashMap<>();
        for (LiveQuoteMonitorInstrumentRuntimeState currentState : instrumentStates) {
            statesByInstrumentCode.put(currentState.instrumentCode(), currentState);
        }
        statesByInstrumentCode.put(instrumentState.instrumentCode(), instrumentState);

        return withInstrumentStates(statesByInstrumentCode.values()
                .stream()
                .toList()
        );
    }

    private static List<LiveQuoteMonitorInstrumentRuntimeState> copyAndValidateInstrumentStates(
            List<LiveQuoteMonitorInstrumentRuntimeState> instrumentStates
    ) {
        Objects.requireNonNull(instrumentStates, "instrumentStates must not be null");

        Map<InstrumentCode, LiveQuoteMonitorInstrumentRuntimeState> statesByInstrumentCode =
                new LinkedHashMap<>();
        for (LiveQuoteMonitorInstrumentRuntimeState state : instrumentStates) {
            LiveQuoteMonitorInstrumentRuntimeState stateToCheck =
                    Objects.requireNonNull(state, "instrumentState must not be null");
            LiveQuoteMonitorInstrumentRuntimeState previous = statesByInstrumentCode.put(
                    stateToCheck.instrumentCode(),
                    stateToCheck
            );
            if (previous != null) {
                throw new IllegalArgumentException(
                        "Duplicate Live Quote Monitor runtime state detected for instrument code '" +
                                stateToCheck.instrumentCode().value() + "'"
                );
            }
        }

        return List.copyOf(statesByInstrumentCode.values());
    }
}
