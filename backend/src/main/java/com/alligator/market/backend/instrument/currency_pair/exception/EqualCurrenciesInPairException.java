package com.alligator.market.backend.instrument.currency_pair.exception;

/**
 * Валютные коды пары не могут совпадать.
 */
public class EqualCurrenciesInPairException extends RuntimeException {
    public EqualCurrenciesInPairException(String currencyCode) {
        super("Both currency codes are '%s'".formatted(currencyCode));
    }
}
