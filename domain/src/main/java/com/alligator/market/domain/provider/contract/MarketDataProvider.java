package com.alligator.market.domain.provider.contract;

import com.alligator.market.domain.instrument.contract.Instrument;
import com.alligator.market.domain.instrument.type.InstrumentType;
import com.alligator.market.domain.provider.contract.descriptor.ProviderDescriptor;
import com.alligator.market.domain.provider.contract.policy.ProviderPolicy;
import com.alligator.market.domain.provider.contract.settings.ProviderSettings;
import com.alligator.market.domain.quote.QuoteTick;
import org.reactivestreams.Publisher;

import java.util.Set;

/**
 * Контракт провайдера рыночных данных.
 */
public sealed interface MarketDataProvider permits AbstractMarketDataProvider {

    /** Технический код провайдера. */
    String providerCode();

    /** Дескриптор провайдера: иммутабельный набор статических атрибутов (только отображение). */
    ProviderDescriptor descriptor();

    /** Политика провайдера: иммутабельные параметры, которые использует бизнес-логика. */
    ProviderPolicy policy();

    /** Настройки провайдера: параметры, которые разрешено менять из frontend. */
    @SuppressWarnings( "unused")
    ProviderSettings settings(); // Точка роста, без реализации (заглушка)

    /** Иммутабельный набор кодов поддерживаемых провайдером инструментов. */
    Set<String> instrumentsCodes();

    /** Иммутабельный набор типов поддерживаемых провайдером инструментов. */
    Set<InstrumentType> instrumentsTypes();

    /** Унифицированная операция получения котировок для любого поддерживаемого инструмента. */
    <I extends Instrument> Publisher<QuoteTick> quote(I instrument);
}
