package com.alligator.market.domain.process.quotemonitor.runtime.instrument;

import com.alligator.market.domain.instrument.vo.InstrumentCode;
import com.alligator.market.domain.source.vo.SourceCode;

import java.util.Objects;
import java.util.Optional;

public final class QuoteMonitorInstrumentRuntimeState {
    private final InstrumentCode instrumentCode;
    private final SourceCode sourceCode;
    private final QuoteMonitorInstrumentRuntimeStatus status;
    private final String detail;

    public QuoteMonitorInstrumentRuntimeState(
            InstrumentCode instrumentCode,
            SourceCode sourceCode,
            QuoteMonitorInstrumentRuntimeStatus status,
            String detail
    ) {
        this.instrumentCode = Objects.requireNonNull(instrumentCode, "instrumentCode must not be null");
        this.sourceCode = sourceCode;
        this.status = Objects.requireNonNull(status, "status must not be null");
        this.detail = normalizeDetail(detail);
    }

    public static QuoteMonitorInstrumentRuntimeState stopped(InstrumentCode instrumentCode) {
        return new QuoteMonitorInstrumentRuntimeState(
                instrumentCode,
                null,
                QuoteMonitorInstrumentRuntimeStatus.STOPPED,
                null
        );
    }

    public static QuoteMonitorInstrumentRuntimeState waitingForQuote(
            InstrumentCode instrumentCode,
            SourceCode sourceCode
    ) {
        return new QuoteMonitorInstrumentRuntimeState(
                instrumentCode,
                sourceCode,
                QuoteMonitorInstrumentRuntimeStatus.WAITING_FOR_QUOTE,
                null
        );
    }

    public static QuoteMonitorInstrumentRuntimeState live(
            InstrumentCode instrumentCode,
            SourceCode sourceCode
    ) {
        return new QuoteMonitorInstrumentRuntimeState(
                instrumentCode,
                sourceCode,
                QuoteMonitorInstrumentRuntimeStatus.LIVE,
                null
        );
    }

    public static QuoteMonitorInstrumentRuntimeState issue(
            InstrumentCode instrumentCode,
            SourceCode sourceCode,
            QuoteMonitorInstrumentRuntimeStatus status,
            String detail
    ) {
        if (status == QuoteMonitorInstrumentRuntimeStatus.STOPPED
                || status == QuoteMonitorInstrumentRuntimeStatus.WAITING_FOR_QUOTE
                || status == QuoteMonitorInstrumentRuntimeStatus.LIVE) {
            throw new IllegalArgumentException("status must describe a runtime issue: " + status);
        }

        return new QuoteMonitorInstrumentRuntimeState(
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

    public QuoteMonitorInstrumentRuntimeStatus status() {
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
