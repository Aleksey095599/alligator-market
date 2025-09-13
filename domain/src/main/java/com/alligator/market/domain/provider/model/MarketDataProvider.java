package com.alligator.market.domain.provider.model;

import com.alligator.market.domain.instrument.base.contract.Instrument;
import com.alligator.market.domain.provider.model.info.ProviderStaticInfo;
import com.alligator.market.domain.quote.QuoteTick;
import org.reactivestreams.Publisher;

/**
 * Контракт провайдера рыночных данных.
 */
public interface MarketDataProvider {

    /** Статическая информация о провайдере. */
    ProviderStaticInfo staticInfo();

    /** Унифицированная операция получения котировок для любого поддерживаемого инструмента. */
    <I extends Instrument> Publisher<QuoteTick> quote(I instrument);
}
