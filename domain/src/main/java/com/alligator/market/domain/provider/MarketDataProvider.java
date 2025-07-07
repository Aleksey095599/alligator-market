package com.alligator.market.domain.provider;

import com.alligator.market.domain.instrument.forex.Instrument;
import com.alligator.market.domain.quotes.QuoteTick;

/**
 * Единый контракт адаптера для всех провайдеров рыночных данных.
 */
public interface MarketDataProvider {

    // Уникальное имя провайдера
    String name();

    // Режим провайдера: PULL или PUSH
    String mode();

    // Метод получения рыночных данных: API_POLL,
    String method();

    /* Подписка на стрим котировок указанного инструмента. */
    reactor.core.publisher.Flux<QuoteTick> subscribe(Instrument instrument);
}
