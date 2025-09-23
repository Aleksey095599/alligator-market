package com.alligator.market.domain.provider.handler.contract.exception;

/**
 * Инструмент не поддерживается обработчиком.
 */
public class InstrumentNotSupportedException extends RuntimeException {
    public InstrumentNotSupportedException(
            String instrumentCode,
            String handlerCode
    ) {
        super("Instrument %s not supported by handler %s".formatted(instrumentCode, handlerCode));
    }
}
