package com.alligator.market.domain.quote.stream.exeptions;

/**
 * Бросается, если провайдер не смог вернуть актуальную котировку.
 */
public class QuoteUnavailableException extends RuntimeException {
    public QuoteUnavailableException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
