package com.alligator.market.domain.provider.exception;

import com.alligator.market.domain.instrument.base.Instrument;

/**
 * Неподдерживаемый класс инструмента.
 */
public class InstrumentNotSupportedException extends RuntimeException {
    public InstrumentNotSupportedException(Class<? extends Instrument> clazz, String providerCode) {
        super("Instrument %s not supported by %s".formatted(clazz.getSimpleName(), providerCode));
    }
}
