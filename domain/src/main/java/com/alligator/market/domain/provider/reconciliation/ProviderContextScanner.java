package com.alligator.market.domain.provider.reconciliation;

import com.alligator.market.domain.provider.contract.descriptor.ProviderDescriptor;

import java.util.List;

/**
 * Сканер контекста приложения для получения данных о провайдерах рыночных данных.
 */
public interface ProviderContextScanner {

    /** Вернуть список дескрипторов провайдеров. */
    List<ProviderDescriptor> providerDescriptors();
}
