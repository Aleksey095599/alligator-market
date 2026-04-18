package com.alligator.market.domain.provider.model;

import com.alligator.market.domain.instrument.base.Instrument;
import com.alligator.market.domain.provider.model.passport.ProviderPassport;
import com.alligator.market.domain.provider.model.policy.ProviderPolicy;
import com.alligator.market.domain.provider.model.vo.ProviderCode;
import com.alligator.market.domain.marketdata.tick.model.QuoteTick;
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
