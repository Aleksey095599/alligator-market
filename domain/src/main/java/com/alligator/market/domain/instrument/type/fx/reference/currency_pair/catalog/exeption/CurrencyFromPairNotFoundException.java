package com.alligator.market.domain.instrument.type.fx.reference.currency_pair.catalog.exeption;

/**
 * Валюта из пары не найдена.
 */
public class CurrencyFromPairNotFoundException extends RuntimeException {
    public CurrencyFromPairNotFoundException(String currencyCode) {
        super("Currency '%s' not found".formatted(currencyCode));
    }
}
