package com.alligator.market.backend.instrument.currency_pair.exception;

/**
 * Не найдена одна из валют компонент для соответствующей валютной пары.
 */
public class CurrencyFromPairNotFoundException extends RuntimeException {
    public CurrencyFromPairNotFoundException(String currencyCode) {
        super("Currency '%s' not found".formatted(currencyCode));
    }
}
