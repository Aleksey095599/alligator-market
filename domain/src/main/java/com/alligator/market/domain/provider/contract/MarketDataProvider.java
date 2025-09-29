package com.alligator.market.domain.provider.contract;

import com.alligator.market.domain.instrument.contract.Instrument;
import com.alligator.market.domain.instrument.type.InstrumentType;
import com.alligator.market.domain.provider.contract.descriptor.ProviderDescriptor;
import com.alligator.market.domain.provider.contract.policy.ProviderPolicy;
import com.alligator.market.domain.quote.QuoteTick;
import org.reactivestreams.Publisher;

import java.util.Set;

/**
 * Контракт провайдера рыночных данных.
 */
public sealed interface MarketDataProvider permits AbstractMarketDataProvider {

    /** Технический код провайдера. */
    String providerCode();

    /** Отображаемое имя провайдера (user friendly). */
    String displayName();

    /** Дескриптор провайдера: иммутабельный набор статических атрибутов. */
    ProviderDescriptor descriptor();

    /** Иммутабельные настройки провайдера. */
    ProviderPolicy settings();

    /** Иммутабельный набор кодов поддерживаемых провайдером инструментов. */
    Set<String> instrumentsCodes();

    /** Иммутабельный набор типов поддерживаемых провайдером инструментов. */
    Set<InstrumentType> instrumentsTypes();

    /** Унифицированная операция получения котировок для любого поддерживаемого инструмента. */
    <I extends Instrument> Publisher<QuoteTick> quote(I instrument);
}
