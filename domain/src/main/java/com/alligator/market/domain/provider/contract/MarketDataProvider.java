package com.alligator.market.domain.provider.contract;

import com.alligator.market.domain.instrument.contract.Instrument;
import com.alligator.market.domain.instrument.type.InstrumentType;
import com.alligator.market.domain.provider.code.ProviderCode;
import com.alligator.market.domain.provider.contract.descriptor.ProviderDescriptor;
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
     * Дескриптор провайдера: иммутабельный набор статических атрибутов (только отображение).
     */
    ProviderDescriptor descriptor();

    /**
     * "Политика провайдера": иммутабельные параметры, которые использует бизнес-логика.
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
    Set<String> instrumentsCodes(); // <-- TODO: нужен ли?

    /**
     * Иммутабельный набор типов поддерживаемых инструментов.
     */
    @SuppressWarnings("unused")
    Set<InstrumentType> instrumentsTypes(); // <-- TODO: нужен ли?

    /**
     * Унифицированная операция получения котировок для любого поддерживаемого инструмента.
     */
    <I extends Instrument> Publisher<QuoteTick> quote(I instrument);
}
