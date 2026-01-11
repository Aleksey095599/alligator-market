package com.alligator.market.domain.provider.reconciliation.scanner;

import com.alligator.market.domain.provider.code.ProviderCode;
import com.alligator.market.domain.provider.reconciliation.dto.ProviderSnapshot;

import java.util.Map;

/**
 * Сканер контекста приложения для получения данных о провайдерах рыночных данных.
 */
public sealed interface ProviderContextScanner permits AbstractProviderContextScanner {

    /**
     * Вернуть карту снимков провайдеров, индексированную по коду провайдера.
     *
     * <p>Снимок провайдера {@link ProviderSnapshot} содержит данные о провайдере: код провайдера, паспорт, "политику".
     */
    Map<ProviderCode, ProviderSnapshot> providerSnapshots(); // <-- TODO: убрать после реализации паспорта провайдера


}
