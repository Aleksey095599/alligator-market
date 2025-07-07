package com.alligator.market.backend.instrument.forex.currency_pair.exception;

/**
 * Валютные коды пары не могут совпадать.
 */
public class EqualCurrenciesInPairException extends RuntimeException {
    public EqualCurrenciesInPairException(String code) {
        super("Both currency codes are '%s'".formatted(code));
    }
}
