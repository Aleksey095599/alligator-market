package com.alligator.market.domain.provider.exception;

import com.alligator.market.domain.instrument.base.Instrument;

/**
 * Дублирование обработчика по классу инструмента.
 */
public class ProviderInstrumentHandlerDuplicateException extends RuntimeException {
    public ProviderInstrumentHandlerDuplicateException(Class<? extends Instrument> clazz) {
        super("Duplicate handler for instrument class: %s".formatted(clazz.getSimpleName()));
    }
}
