package com.alligator.market.domain.provider.reconciliation;

import com.alligator.market.domain.provider.handler.contract.InstrumentHandler;
import com.alligator.market.domain.provider.profile.model.Profile;

import java.util.Map;

/**
 * Контракт сканера контекста провайдеров, извлекающего их профили.
 */
public interface ProviderContextScanner {

    /**
     * Вернуть карту профилей из контекста,
     * где ключ — код провайдера.
     */
    Map<String, Profile> getProfiles();

    /**
     * Вернуть обработчики (handlers) для всех финансовых инструментов,
     * где ключ — код провайдера.
     * */
    Map<String, InstrumentHandler> getHandlers();
}
