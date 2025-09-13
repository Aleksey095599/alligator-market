package com.alligator.market.domain.provider.contract;

import com.alligator.market.domain.instrument.contract.Instrument;
import com.alligator.market.domain.provider.contract.descriptor.ProviderDescriptor;
import com.alligator.market.domain.quote.QuoteTick;
import org.reactivestreams.Publisher;

/**
 * Контракт провайдера рыночных данных.
 */
public interface MarketDataProvider {

    /** Дескриптор провайдера: неизменяемый набор статических атрибутов. */
    ProviderDescriptor descriptor();

    /** Унифицированная операция получения котировок для любого поддерживаемого инструмента. */
    <I extends Instrument> Publisher<QuoteTick> quote(I instrument);
}
