package com.alligator.market.domain.provider.exception;

import com.alligator.market.domain.instrument.vo.InstrumentCode;
import com.alligator.market.domain.provider.vo.ProviderCode;

import java.util.Objects;

/**
 * Обработчик для инструмента не найден.
 */
public final class HandlerNotFoundException extends IllegalStateException {

    public HandlerNotFoundException(InstrumentCode instrumentCode, ProviderCode providerCode) {
        super("Handler not found (instrumentCode=%s, providerCode=%s)".formatted(
                Objects.requireNonNull(instrumentCode, "instrumentCode must not be null").value(),
                Objects.requireNonNull(providerCode, "providerCode must not be null").value()
        ));
    }
}
