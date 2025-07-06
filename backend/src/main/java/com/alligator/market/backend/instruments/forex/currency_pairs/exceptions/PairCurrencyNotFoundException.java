package com.alligator.market.backend.instruments.forex.currency_pairs.exceptions;

/**
 * Не найдена одна из валют компонент для соответствующей валютной пары.
 */
public class PairCurrencyNotFoundException extends RuntimeException {
    public PairCurrencyNotFoundException(String code) {
        super("Currency '%s' not found".formatted(code));
    }
}
