package com.alligator.market.domain.provider;

import com.alligator.market.domain.instrument.forex.Instrument;
import com.alligator.market.domain.quotes.QuoteTick;
import reactor.core.publisher.Flux;

/**
 * Единый контракт адаптера для всех провайдеров рыночных данных.
 */
public interface MarketDataProvider {

    String code();

    DeliveryMode deliveryMode();

    AccessMethod accessMethod();

    Flux<QuoteTick> subscribe(Instrument instrument);
}
