package com.alligator.market.domain.provider.reconciliation;

import com.alligator.market.domain.instrument.contract.Instrument;
import com.alligator.market.domain.provider.contract.descriptor.ProviderDescriptor;
import com.alligator.market.domain.provider.handler.contract.InstrumentHandler;
import com.alligator.market.domain.provider.contract.MarketDataProvider;

import java.util.List;

/**
 * Контракт сканера контекста приложения касательно данных о провайдерах рыночных данных.
 */
public interface ProviderContextScanner {

    /** Вернуть список дескрипторов провайдеров {@link ProviderDescriptor}. */
    List<ProviderDescriptor> providerDescriptors();

    /** Вернуть список обработчиков {@link InstrumentHandler}. */
    List<InstrumentHandler<? extends MarketDataProvider, ? extends Instrument>> instrumentHandlers();
}
