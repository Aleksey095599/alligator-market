package com.alligator.market.domain.instrument.type.fx.reference.currency_pair.catalog.exeption;

/**
 * Валютная пара уже существует.
 */
public class DuplicatePairException extends RuntimeException {
    public DuplicatePairException(String pairCode) {
        super("Currency pair '%s' already exists".formatted(pairCode));
    }
}
