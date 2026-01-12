package com.alligator.market.domain.provider.reconciliation.scanner;

import com.alligator.market.domain.provider.code.ProviderCode;
import com.alligator.market.domain.provider.contract.passport.ProviderPassport;
import com.alligator.market.domain.provider.reconciliation.dto.ProviderSnapshot;

import java.util.Map;

/**
 * Сканер контекста приложения.
 *
 * <p>Назначение: извлечение из контекста приложения данных о провайдерах рыночных данных, в частности — паспортов
 * провайдеров {@link ProviderPassport}.</p>
 */
public sealed interface ProviderContextScanner permits AbstractProviderContextScanner {

    /**
     * Возвращает карту снимков провайдеров, индексированную по коду провайдера.
     *
     * <p>Снимок провайдера {@link ProviderSnapshot} содержит данные о провайдере: код провайдера, паспорт, "политику".</p>
     */
    @Deprecated(forRemoval = true)
    Map<ProviderCode, ProviderSnapshot> providerSnapshots();

    /**
     * Возвращает карту паспортов провайдеров, индексированную по коду провайдера.
     */
    Map<ProviderCode, ProviderPassport> providerPassports();
}
