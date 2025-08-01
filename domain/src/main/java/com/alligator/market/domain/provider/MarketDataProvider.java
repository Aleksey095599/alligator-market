package com.alligator.market.domain.provider;

import com.alligator.market.domain.instrument.Instrument;
import com.alligator.market.domain.provider.profile.ProviderProfile;
import com.alligator.market.domain.quote.QuoteTick;
import reactor.core.publisher.Flux;

/**
 * Единый контракт адаптера для всех провайдеров рыночных данных.
 */
public interface MarketDataProvider {

    //===========================
    //    Профиль провайдера
    //===========================

    /** Возвращает профиль провайдера. */
    ProviderProfile profile();

    //===========================
    // Реактивный поток котировок
    //===========================

    /** Возвращает поток котировок в виде {@link QuoteTick}. */
    Flux<QuoteTick> streamQuotes(Instrument instrument);
}
