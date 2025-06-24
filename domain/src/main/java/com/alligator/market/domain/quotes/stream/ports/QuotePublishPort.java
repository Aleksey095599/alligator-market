package com.alligator.market.domain.quotes.stream.ports;

import com.alligator.market.domain.quotes.stream.QuoteTick;

/**
 * Выходной порт «сложить тик куда‑то».
 */
public interface QuotePublishPort {

    /* Публикует тик котировки в целевую систему. */
    void publish(QuoteTick tick);
    
}
