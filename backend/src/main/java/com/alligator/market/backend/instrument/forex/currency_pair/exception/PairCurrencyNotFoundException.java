package com.alligator.market.backend.instrument.forex.currency_pair.exception;

/**
 * Не найдена одна из валют компонент для соответствующей валютной пары.
 */
public class PairCurrencyNotFoundException extends RuntimeException {
    public PairCurrencyNotFoundException(String code) {
        super("Currency '%s' not found".formatted(code));
    }
}
