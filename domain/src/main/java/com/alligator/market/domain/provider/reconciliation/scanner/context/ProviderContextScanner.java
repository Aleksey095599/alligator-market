package com.alligator.market.domain.provider.reconciliation.scanner.context;

import com.alligator.market.domain.provider.code.ProviderCode;
import com.alligator.market.domain.provider.contract.passport.ProviderPassport;

import java.util.Map;

/**
 * Сканер контекста приложения для извлечения данных о провайдерах рыночных данных.
 *
 * <p>Только {@link AbstractProviderContextScanner} разрешено реализовать этот интерфейс.</p>
 */
public sealed interface ProviderContextScanner permits AbstractProviderContextScanner {

    /**
     * Возвращает из контекста приложения карту паспортов провайдеров, индексированную по коду провайдера.
     */
    Map<ProviderCode, ProviderPassport> providerPassports();
}
