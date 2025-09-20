package com.alligator.market.domain.provider.reconciliation;

import com.alligator.market.domain.provider.contract.descriptor.ProviderDescriptor;

import java.util.List;

/**
 * Контракт сканера контекста приложения касательно провайдеров рыночных данных.
 */
public interface ProviderContextScanner {

    /** Вернуть список дескрипторов провайдеров {@link ProviderDescriptor}. */
    List<ProviderDescriptor> providerDescriptors();
}
