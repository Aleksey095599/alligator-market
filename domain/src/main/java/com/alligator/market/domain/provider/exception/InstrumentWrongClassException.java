package com.alligator.market.domain.provider.exception;

/**
 * Класс инструмента не соответствует обработчику.
 */
public class InstrumentWrongClassException extends RuntimeException {

    public InstrumentWrongClassException(
            String instrumentCode,
            Class<?> instrumentClass,
            String handlerCode,
            Class<?> expectedClass
    ) {
        super("Instrument %s has class %s, but provider handler %s expects %s"
                .formatted(instrumentCode, instrumentClass.getName(), handlerCode, expectedClass.getName()));
    }
}
