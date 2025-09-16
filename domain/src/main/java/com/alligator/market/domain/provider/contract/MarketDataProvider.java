package com.alligator.market.domain.provider.contract;

import com.alligator.market.domain.instrument.contract.Instrument;
import com.alligator.market.domain.provider.contract.descriptor.ProviderDescriptor;
import com.alligator.market.domain.provider.contract.settings.ProviderSettings;
import com.alligator.market.domain.quote.QuoteTick;
import org.reactivestreams.Publisher;

/**
 * Контракт провайдера рыночных данных.
 */
public sealed interface MarketDataProvider permits AbstractMarketDataProvider {

    /** Дескриптор провайдера: иммутабельный набор статических атрибутов. */
    ProviderDescriptor descriptor();

    /** Иммутабельные настройки провайдера. */
    ProviderSettings settings();

    /** Унифицированная операция получения котировок для любого поддерживаемого инструмента. */
    <I extends Instrument> Publisher<QuoteTick> quote(I instrument);
}
