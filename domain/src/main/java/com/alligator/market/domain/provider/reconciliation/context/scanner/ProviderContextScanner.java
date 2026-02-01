package com.alligator.market.domain.provider.reconciliation.context.scanner;

import com.alligator.market.domain.provider.model.vo.ProviderCode;
import com.alligator.market.domain.provider.model.passport.ProviderPassport;

import java.util.Map;

/**
 * Сканер контекста приложения для извлечения данных о провайдерах рыночных данных.
 */
public sealed interface ProviderContextScanner permits AbstractProviderContextScanner {

    /**
     * Возвращает из контекста карту паспортов провайдеров, индексированную по коду провайдера.
     */
    Map<ProviderCode, ProviderPassport> providerPassports();
}
