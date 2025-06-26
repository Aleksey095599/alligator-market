package com.alligator.market.domain.quotes.stream.ports;

import com.alligator.market.domain.quotes.stream.QuoteTick;
import com.alligator.market.domain.quotes.stream.exeptions.QuoteUnavailableException;

/**
 * Входной порт для получения тика в режиме PULL.
 */
public interface PullQuoteFeedPort {

    /* Получить последний тик котировки для пары. */
    QuoteTick fetchQuote(String pairCode) throws QuoteUnavailableException;

}
