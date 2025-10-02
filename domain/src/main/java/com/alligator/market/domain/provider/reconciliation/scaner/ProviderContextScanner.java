package com.alligator.market.domain.provider.reconciliation.scaner;

import com.alligator.market.domain.provider.contract.descriptor.ProviderDescriptor;

import java.util.Map;

/**
 * Сканер контекста приложения для получения данных о провайдерах рыночных данных.
 */
public sealed interface ProviderContextScanner permits AbstractProviderContextScanner {

    /** Вернуть карту дескрипторов провайдеров, индексированную по коду провайдера. */
    Map<String, ProviderDescriptor> providerDescriptors();
}
