package com.alligator.market.domain.quotes.ports;

import com.alligator.market.domain.quotes.QuoteTick;

/**
 * Выходной порт для публикации котировки.
 */
public interface QuotePublishPort {

    /* Публикует тик котировки в целевую систему. */
    void publish(QuoteTick tick);
    
}
