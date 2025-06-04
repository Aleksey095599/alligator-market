package com.alligator.core.model;

import reactor.core.publisher.Flux;

/* Входной порт: подписка на поток котировок. */
public interface  QuoteStream {
    Flux<CurrencyQuote> streamAll();
}
