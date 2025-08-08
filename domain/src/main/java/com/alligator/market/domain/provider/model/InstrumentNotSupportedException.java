package com.alligator.market.domain.provider.model;

import com.alligator.market.domain.instrument.model.InstrumentType;

/**
 * Неподдерживаемый тип инструмента.
 */
public class InstrumentNotSupportedException extends RuntimeException {
    public InstrumentNotSupportedException(InstrumentType type, String providerCode) {
        super("Instrument type %s not supported by %s".formatted(type, providerCode));
    }
}
