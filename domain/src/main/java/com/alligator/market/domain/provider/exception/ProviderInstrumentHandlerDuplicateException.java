package com.alligator.market.domain.provider.exception;

import com.alligator.market.domain.instrument.type.InstrumentType;

/**
 * Дублирование обработчика по типу инструмента.
 */
public class ProviderInstrumentHandlerDuplicateException extends RuntimeException {
    public ProviderInstrumentHandlerDuplicateException(InstrumentType type) {
        super("Duplicate handler for instrument type: %s".formatted(type));
    }
}
