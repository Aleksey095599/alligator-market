package com.alligator.market.domain.quotes.ports;

import com.alligator.market.domain.quotes.QuoteTick;

/**
 * Входной порт для получения котировки в режиме PULL.
 */
public interface PullQuotePort {

    /* Получить последний тик котировки для пары. */
    QuoteTick fetchQuote(String pairCode) throws QuoteUnavailableException;

}
