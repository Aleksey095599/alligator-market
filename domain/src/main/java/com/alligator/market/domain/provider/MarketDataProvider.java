package com.alligator.market.domain.provider;

import com.alligator.market.domain.instrument.Instrument;
import com.alligator.market.domain.provider.passport.ProviderPassport;
import com.alligator.market.domain.provider.policy.ProviderPolicy;
import com.alligator.market.domain.provider.vo.ProviderCode;
import com.alligator.market.domain.marketdata.tick.old.QuoteTick;
import org.reactivestreams.Publisher;

/**
 * Доменный контракт провайдера рыночных данных.
 */
public interface MarketDataProvider {

    /**
     * Уникальный код провайдера (идентификатор).
     */
    ProviderCode providerCode();

    /**
     * Паспорт провайдера.
     */
    ProviderPassport passport();

    /**
     * Политика провайдера.
     */
    ProviderPolicy policy();

    /**
     * Поток котировок для заданного инструмента.
     */
    <I extends Instrument> Publisher<QuoteTick> quote(I instrument);
}
