package com.alligator.market.domain.provider.contract;

import com.alligator.market.domain.instrument.code.InstrumentCode;
import com.alligator.market.domain.instrument.contract.Instrument;
import com.alligator.market.domain.instrument.type.InstrumentType;
import com.alligator.market.domain.provider.code.ProviderCode;
import com.alligator.market.domain.provider.contract.passport.ProviderPassport;
import com.alligator.market.domain.provider.contract.policy.ProviderPolicy;
import com.alligator.market.domain.provider.contract.settings.ProviderSettings;
import com.alligator.market.domain.quote.tick.model.QuoteTick;
import org.reactivestreams.Publisher;

import java.util.Set;

/**
 * Контракт провайдера рыночных данных.
 */
public sealed interface MarketDataProvider permits AbstractMarketDataProvider {

    /**
     * Технический код провайдера.
     */
    ProviderCode providerCode();

    /**
     * Паспорт провайдера: иммутабельный набор статических атрибутов (только отображение).
     */
    ProviderPassport passport();

    /**
     * Политика провайдера: иммутабельные параметры, которые использует бизнес-логика.
     */
    ProviderPolicy policy();

    /**
     * Настройки провайдера: параметры, которые разрешено менять из frontend.
     */
    @SuppressWarnings("unused")
    ProviderSettings settings(); // <-- Точка роста, без реализации (заглушка)

    /**
     * Иммутабельный набор кодов поддерживаемых инструментов.
     */
    @SuppressWarnings("unused")
    Set<InstrumentCode> instrumentsCodes();

    /**
     * Иммутабельный набор типов поддерживаемых инструментов.
     */
    @SuppressWarnings("unused")
    Set<InstrumentType> instrumentsTypes();

    /**
     * Унифицированная операция получения котировок для финансового инструмента.
     */
    <I extends Instrument> Publisher<QuoteTick> quote(I instrument);
}
