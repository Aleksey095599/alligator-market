package com.alligator.market.domain.instrument.currency_pair.catalog.exeption;

/**
 * В паре указана одна и та же валюта.
 */
public class EqualCurrenciesInPairException extends RuntimeException {
    public EqualCurrenciesInPairException(String currencyCode) {
        super("Both currency codes are '%s'".formatted(currencyCode));
    }
}
