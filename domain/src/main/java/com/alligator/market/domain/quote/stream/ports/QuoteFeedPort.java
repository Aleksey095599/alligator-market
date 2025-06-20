package com.alligator.market.domain.quote.stream.ports;

import com.alligator.market.domain.quote.stream.QuoteTick;
import com.alligator.market.domain.quote.stream.exeptions.QuoteUnavailableException;

/**
 * Абстракция источника котировок.
 */
public interface QuoteFeedPort {

    /* Получить последний тик котировки для пары. */
    QuoteTick fetchQuote(String pairCode) throws QuoteUnavailableException;

}
