package com.alligator.market.domain.provider.model;

import com.alligator.market.domain.instrument.model.Instrument;
import com.alligator.market.domain.provider.model.vo.ProviderCode;
import com.alligator.market.domain.provider.model.passport.ProviderPassport;
import com.alligator.market.domain.provider.model.policy.ProviderPolicy;
import com.alligator.market.domain.quote.tick.model.QuoteTick;
import org.reactivestreams.Publisher;

/**
 * Провайдер рыночных данных.
 *
 * <p>Реализация разрешена только через {@link AbstractMarketDataProvider}.</p>
 */
public sealed interface MarketDataProvider permits AbstractMarketDataProvider {

    /**
     * Код провайдера.
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
     * Унифицированная операция получения котировок для финансового инструмента.
     */
    <I extends Instrument> Publisher<QuoteTick> quote(I instrument);
}
