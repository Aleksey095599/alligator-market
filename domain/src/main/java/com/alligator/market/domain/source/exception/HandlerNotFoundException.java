package com.alligator.market.domain.source.exception;

import com.alligator.market.domain.instrument.vo.InstrumentCode;
import com.alligator.market.domain.source.vo.MarketDataSourceCode;

import java.util.Objects;

/**
 * Обработчик для инструмента не найден.
 */
public final class HandlerNotFoundException extends IllegalStateException {

    public HandlerNotFoundException(InstrumentCode instrumentCode, MarketDataSourceCode sourceCode) {
        super("Handler not found (instrumentCode=%s, sourceCode=%s)".formatted(
                Objects.requireNonNull(instrumentCode, "instrumentCode must not be null").value(),
                Objects.requireNonNull(sourceCode, "sourceCode must not be null").value()
        ));
    }
}
