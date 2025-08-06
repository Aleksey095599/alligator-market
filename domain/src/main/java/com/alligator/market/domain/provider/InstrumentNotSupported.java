package com.alligator.market.domain.provider;

import com.alligator.market.domain.instrument.InstrumentType;

/**
 * Неподдерживаемый тип инструмента.
 */
public class InstrumentNotSupported extends RuntimeException {
    public InstrumentNotSupported(InstrumentType type, String providerCode) {
        super("Instrument type %s not supported by %s".formatted(type, providerCode));
    }
}
