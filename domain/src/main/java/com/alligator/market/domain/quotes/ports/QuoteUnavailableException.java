package com.alligator.market.domain.quotes.ports;

/**
 * Бросается, если провайдер не смог вернуть актуальную котировку.
 */
public class QuoteUnavailableException extends RuntimeException {
    public QuoteUnavailableException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
