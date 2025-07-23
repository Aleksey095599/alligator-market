package com.alligator.market.domain.provider;

import com.alligator.market.domain.instrument.Instrument;
import com.alligator.market.domain.quote.QuoteTick;
import reactor.core.publisher.Flux;

/**
 * Единый контракт адаптера для всех провайдеров рыночных данных.
 */
public interface MarketDataProvider {

    //===================
    // Профиль провайдера
    //===================
    ProviderProfile profile();

    //===========================
    // Реактивный поток котировок
    //===========================
    Flux<QuoteTick> streamQuotes(Instrument instrument);
}
