package com.alligator.market.domain.process.quotemonitor.runtime;

import com.alligator.market.domain.instrument.vo.InstrumentCode;

import java.time.Instant;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public final class LiveQuoteMonitorRuntimeSnapshot {
    private final LiveQuoteMonitorRuntimeStatus status;
    private final List<InstrumentCode> monitoredInstrumentCodes;
    private final Instant lastTickAt;

    public LiveQuoteMonitorRuntimeSnapshot(
            LiveQuoteMonitorRuntimeStatus status,
            List<InstrumentCode> monitoredInstrumentCodes,
            Instant lastTickAt
    ) {
        this.status = Objects.requireNonNull(status, "status must not be null");
        this.monitoredInstrumentCodes = List.copyOf(
                Objects.requireNonNull(monitoredInstrumentCodes, "monitoredInstrumentCodes must not be null")
        );
        this.lastTickAt = lastTickAt;
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

    public LiveQuoteMonitorRuntimeSnapshot withStatus(LiveQuoteMonitorRuntimeStatus status) {
        return new LiveQuoteMonitorRuntimeSnapshot(
                status,
                monitoredInstrumentCodes,
                lastTickAt
        );
    }
}
