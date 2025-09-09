package com.alligator.market.domain.provider.exception;

/**
 * Инструмент передан неправильного класса.
 */
public class InstrumentWrongClassException extends RuntimeException {

    public InstrumentWrongClassException(Class<?> expected, Class<?> actual) {
        super("Instrument of class %s is not supported. Expected %s".formatted(actual.getName(), expected.getName()));
    }
}
