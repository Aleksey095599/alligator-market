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
     * где первый ключ — технический код провайдера, второй ключ — отображаемое имя провайдера.
     */
    Map<String, Map<String, Profile>> getProfiles();

    /**
     * Вернуть карту обработчиков (handlers) финансовых инструментов,
     * где первый ключ — код провайдера, второй ключ — тип финансового инструмента.
     */
    Map<String, Map<String, InstrumentHandler>> getHandlers();
}
