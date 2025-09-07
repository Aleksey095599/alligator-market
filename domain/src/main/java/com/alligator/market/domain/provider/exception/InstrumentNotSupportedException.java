package com.alligator.market.domain.provider.exception;

import com.alligator.market.domain.instrument.type.InstrumentType;

/**
 * Неподдерживаемый тип инструмента.
 */
public class InstrumentNotSupportedException extends RuntimeException {
    public InstrumentNotSupportedException(InstrumentType type, String providerCode) {
        super("Instrument type %s not supported by %s".formatted(type, providerCode));
    }
}
