package com.alligator.market.domain.instrument.currency.catalog.exeption;

import com.alligator.market.domain.common.exception.NotFoundException;

/**
 * Валюта не найдена.
 */
public class CurrencyNotFoundException extends NotFoundException {
    public CurrencyNotFoundException(String code) {
        super("Currency '%s' not found".formatted(code));
    }
}
