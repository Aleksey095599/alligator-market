package com.alligator.market.domain.process.quotemonitor.runtime;

import com.alligator.market.domain.instrument.vo.InstrumentCode;
import com.alligator.market.domain.source.vo.SourceCode;

import java.util.Objects;
import java.util.Optional;

public final class LiveQuoteMonitorInstrumentRuntimeState {
    private final InstrumentCode instrumentCode;
    private final SourceCode sourceCode;
    private final LiveQuoteMonitorInstrumentRuntimeStatus status;
    private final String detail;

    public LiveQuoteMonitorInstrumentRuntimeState(
            InstrumentCode instrumentCode,
            SourceCode sourceCode,
            LiveQuoteMonitorInstrumentRuntimeStatus status,
            String detail
    ) {
        this.instrumentCode = Objects.requireNonNull(instrumentCode, "instrumentCode must not be null");
        this.sourceCode = sourceCode;
        this.status = Objects.requireNonNull(status, "status must not be null");
        this.detail = normalizeDetail(detail);
    }

    public static LiveQuoteMonitorInstrumentRuntimeState stopped(InstrumentCode instrumentCode) {
        return new LiveQuoteMonitorInstrumentRuntimeState(
                instrumentCode,
                null,
                LiveQuoteMonitorInstrumentRuntimeStatus.STOPPED,
                null
        );
    }

    public static LiveQuoteMonitorInstrumentRuntimeState waitingForQuote(
            InstrumentCode instrumentCode,
            SourceCode sourceCode
    ) {
        return new LiveQuoteMonitorInstrumentRuntimeState(
                instrumentCode,
                sourceCode,
                LiveQuoteMonitorInstrumentRuntimeStatus.WAITING_FOR_QUOTE,
                null
        );
    }

    public static LiveQuoteMonitorInstrumentRuntimeState live(
            InstrumentCode instrumentCode,
            SourceCode sourceCode
    ) {
        return new LiveQuoteMonitorInstrumentRuntimeState(
                instrumentCode,
                sourceCode,
                LiveQuoteMonitorInstrumentRuntimeStatus.LIVE,
                null
        );
    }

    public static LiveQuoteMonitorInstrumentRuntimeState issue(
            InstrumentCode instrumentCode,
            SourceCode sourceCode,
            LiveQuoteMonitorInstrumentRuntimeStatus status,
            String detail
    ) {
        if (status == LiveQuoteMonitorInstrumentRuntimeStatus.STOPPED
                || status == LiveQuoteMonitorInstrumentRuntimeStatus.WAITING_FOR_QUOTE
                || status == LiveQuoteMonitorInstrumentRuntimeStatus.LIVE) {
            throw new IllegalArgumentException("status must describe a runtime issue: " + status);
        }

        return new LiveQuoteMonitorInstrumentRuntimeState(
                instrumentCode,
                sourceCode,
                status,
                detail
        );
    }

    public InstrumentCode instrumentCode() {
        return instrumentCode;
    }

    public Optional<SourceCode> sourceCode() {
        return Optional.ofNullable(sourceCode);
    }

    public LiveQuoteMonitorInstrumentRuntimeStatus status() {
        return status;
    }

    public Optional<String> detail() {
        return Optional.ofNullable(detail);
    }

    private static String normalizeDetail(String detail) {
        if (detail == null) {
            return null;
        }

        String normalized = detail.trim();
        if (normalized.isEmpty()) {
            return null;
        }

        return normalized;
    }
}
