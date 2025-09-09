package com.alligator.market.domain.provider.exception;

import com.alligator.market.domain.instrument.type.InstrumentType;

/**
 * Инструмент не поддерживается.
 */
public class InstrumentNotSupportedException extends RuntimeException {
    public InstrumentNotSupportedException(InstrumentType type, String providerCode) {
        super("Instrument %s not supported by %s".formatted(type, providerCode));
    }
}
