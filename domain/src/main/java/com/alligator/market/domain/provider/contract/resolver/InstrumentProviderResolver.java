package com.alligator.market.domain.provider.contract.resolver;

import com.alligator.market.domain.instrument.contract.Instrument;

/**
 * Сервис для определения провайдера рыночных данных для заданного инструмента.
 */
public interface InstrumentProviderResolver {


    <I extends Instrument> MarketDataProviderCode resolveProvider(I instrument);
}
