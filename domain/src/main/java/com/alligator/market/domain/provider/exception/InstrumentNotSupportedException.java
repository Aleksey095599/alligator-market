package com.alligator.market.domain.provider.exception;

/**
 * Инструмент не поддерживается обработчиком.
 */
public class InstrumentNotSupportedException extends RuntimeException {
    public InstrumentNotSupportedException(String instrumentCode, String handlerCode, String providerCode) {
        super("Instrument %s not supported by handler %s of provider %s".formatted(instrumentCode, handlerCode, providerCode));
    }
}
