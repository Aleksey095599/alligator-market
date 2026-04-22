package com.alligator.market.domain.provider;

import com.alligator.market.domain.instrument.base.Instrument;
import com.alligator.market.domain.provider.passport.ProviderPassport;
import com.alligator.market.domain.provider.policy.ProviderPolicy;
import com.alligator.market.domain.provider.vo.HandlerCode;
import com.alligator.market.domain.provider.vo.ProviderCode;
import com.alligator.market.domain.marketdata.tick.model.QuoteTick;
import org.reactivestreams.Publisher;

import java.util.Set;

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
     * Список кодов обработчиков, подключенных к провайдеру.
     */
    Set<HandlerCode> attachedHandlerCodes();

    /**
     * Поток котировок для заданного инструмента.
     */
    <I extends Instrument> Publisher<QuoteTick> quote(I instrument);
}
