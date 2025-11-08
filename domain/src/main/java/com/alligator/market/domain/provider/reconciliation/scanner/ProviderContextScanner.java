package com.alligator.market.domain.provider.reconciliation.scanner;

import com.alligator.market.domain.provider.reconciliation.dto.ProviderSnapshot;

import java.util.Map;

/**
 * Сканер контекста приложения для получения данных о провайдерах рыночных данных.
 */
public sealed interface ProviderContextScanner permits AbstractProviderContextScanner {

    /**
     * Вернуть карту снимков провайдеров (descriptor + policy), индексированную по коду провайдера.
     */
    Map<String, ProviderSnapshot> providerSnapshots();
}
