package com.alligator.market.domain.provider.reconciliation;

import com.alligator.market.domain.provider.handler.contract.InstrumentHandler;
import com.alligator.market.domain.provider.profile.model.Profile;

import java.util.List;

/**
 * Контракт сканера контекста провайдеров, извлекающего их профили.
 */
public interface ProviderContextScanner {

    /**
     * Вернуть список профилей из контекста.
     */
    List<Profile> getProfiles();

    /**
     * Вернуть список обработчиков финансовых инструментов из контекста.
     */
    List<InstrumentHandler> getHandlers();
}
