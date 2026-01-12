package com.alligator.market.domain.provider.reconciliation.scanner;

import com.alligator.market.domain.provider.code.ProviderCode;
import com.alligator.market.domain.provider.contract.passport.ProviderPassport;
import com.alligator.market.domain.provider.reconciliation.dto.ProviderSnapshot;

import java.util.Map;

/**
 * Сканер контекста приложения.
 *
 * <p>Назначение: извлекать из контекста приложения данные о провайдерах рыночных данных, в частности, – паспорта
 * провайдеров {@link ProviderPassport}.</p>
 */
public sealed interface ProviderContextScanner permits AbstractProviderContextScanner {

    /**
     * Вернуть карту снимков провайдеров, индексированную по коду провайдера.
     *
     * <p>Снимок провайдера {@link ProviderSnapshot} содержит данные о провайдере: код провайдера, паспорт, "политику".
     */
    @Deprecated(forRemoval = true)
    Map<ProviderCode, ProviderSnapshot> providerSnapshots();

    /**
     * Вернуть карту паспортов провайдеров, индексированную по коду провайдера.
     */
    Map<ProviderCode, ProviderPassport> providerPassports();
}
