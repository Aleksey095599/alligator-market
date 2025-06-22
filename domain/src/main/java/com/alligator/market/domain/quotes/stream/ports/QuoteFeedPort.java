package com.alligator.market.domain.quotes.stream.ports;

import com.alligator.market.domain.quotes.stream.QuoteTick;
import com.alligator.market.domain.quotes.stream.exeptions.QuoteUnavailableException;

/**
 * Абстракция источника котировок.
 */
public interface QuoteFeedPort {

    /* Получить последний тик котировки для пары. */
    QuoteTick fetchQuote(String pairCode) throws QuoteUnavailableException;

}
