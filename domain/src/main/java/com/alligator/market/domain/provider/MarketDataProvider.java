package com.alligator.market.domain.provider;

import com.alligator.market.domain.instrument.Instrument;
import com.alligator.market.domain.marketdata.tick.level.source.SourceMarketDataTick;
import com.alligator.market.domain.provider.passport.ProviderPassport;
import com.alligator.market.domain.provider.policy.ProviderPolicy;
import com.alligator.market.domain.provider.vo.ProviderCode;
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
     * Streams source-level market data ticks for the given instrument.
     */
    <I extends Instrument> Publisher<SourceMarketDataTick> streamSourceTicks(I instrument);
}
