package com.alligator.market.domain.provider.reconciliation;

import com.alligator.market.domain.instrument.contract.Instrument;
import com.alligator.market.domain.provider.contract.descriptor.ProviderDescriptor;
import com.alligator.market.domain.provider.handler.contract.InstrumentHandler;
import com.alligator.market.domain.provider.contract.MarketDataProvider;

import java.util.List;

/**
 * Контракт сканера контекста провайдеров, извлекающего их профили.
 */
public interface ProviderContextScanner {

    /** Вернуть список профилей из контекста. */
    List<ProviderDescriptor> getProfiles();

    /** Вернуть список обработчиков финансовых инструментов из контекста. */
    List<InstrumentHandler<? extends MarketDataProvider, ? extends Instrument>> getHandlers();
}
