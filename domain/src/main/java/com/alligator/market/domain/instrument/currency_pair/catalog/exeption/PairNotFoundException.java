package com.alligator.market.domain.instrument.currency_pair.catalog.exeption;

import com.alligator.market.domain.common.exception.NotFoundException;

/**
 * Валютная пара не найдена.
 */
public class PairNotFoundException extends NotFoundException {
    public PairNotFoundException(String pairCode) {
        super("Currency pair '%s' not found".formatted(pairCode));
    }
}
