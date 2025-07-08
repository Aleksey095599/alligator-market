package com.alligator.market.domain.quote.ports;

import com.alligator.market.domain.quote.QuoteTick;

/**
 * Выходной порт для публикации котировки.
 */
public interface QuotePublishPort {

    /* Публикует тик котировки в целевую систему. */
    void publish(QuoteTick tick);
    
}
