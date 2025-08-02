package com.alligator.market.domain.provider.model;

import com.alligator.market.domain.instrument.Instrument;
import com.alligator.market.domain.provider.profile.ProviderProfile;
import com.alligator.market.domain.quote.QuoteTick;
import reactor.core.publisher.Flux;

/**
 * Единый контракт адаптера для всех провайдеров рыночных данных.
 */
public interface MarketDataProvider {

    /** Возвращает профиль провайдера. */
    ProviderProfile profile();

    /** Возвращает котировку. */
    Flux<QuoteTick> quote(Instrument instrument);
}
