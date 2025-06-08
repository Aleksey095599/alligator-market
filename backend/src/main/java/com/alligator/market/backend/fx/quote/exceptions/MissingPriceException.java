package com.alligator.market.backend.fx.quote.exceptions;

/* Отсутствует цена в ответе провайдера котировок. */
public class MissingPriceException extends RuntimeException {
    public MissingPriceException(String symbol) {
        super("Price missing for symbol '%s'".formatted(symbol));
    }
}
