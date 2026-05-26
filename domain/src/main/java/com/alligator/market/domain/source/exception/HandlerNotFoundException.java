package com.alligator.market.domain.source.exception;

import com.alligator.market.domain.instrument.vo.InstrumentCode;
import com.alligator.market.domain.source.vo.SourceCode;

import java.util.Objects;

public final class HandlerNotFoundException extends IllegalStateException {
    private final InstrumentCode instrumentCode;
    private final SourceCode sourceCode;

    public HandlerNotFoundException(InstrumentCode instrumentCode, SourceCode sourceCode) {
        super(message(instrumentCode, sourceCode));

        this.instrumentCode = Objects.requireNonNull(instrumentCode, "instrumentCode must not be null");
        this.sourceCode = Objects.requireNonNull(sourceCode, "sourceCode must not be null");
    }

    public InstrumentCode instrumentCode() {
        return instrumentCode;
    }

    public SourceCode sourceCode() {
        return sourceCode;
    }

    private static String message(InstrumentCode instrumentCode, SourceCode sourceCode) {
        return "Handler not found (instrumentCode=%s, sourceCode=%s)".formatted(
                Objects.requireNonNull(instrumentCode, "instrumentCode must not be null").value(),
                Objects.requireNonNull(sourceCode, "sourceCode must not be null").value()
        );
    }
}
