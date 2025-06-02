package com.alligator.market.backend.fx.pairs.service.exceptions;

/** Валюта для валютной пары не найдена. */
public class PairCurrencyNotFoundException extends RuntimeException {
    public PairCurrencyNotFoundException(String code) {
        super("Currency '%s' not found".formatted(code));
    }
}
