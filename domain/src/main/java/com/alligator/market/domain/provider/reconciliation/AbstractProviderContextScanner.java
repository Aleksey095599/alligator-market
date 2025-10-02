package com.alligator.market.domain.provider.reconciliation;

import com.alligator.market.domain.provider.contract.MarketDataProvider;
import com.alligator.market.domain.provider.contract.descriptor.ProviderDescriptor;
import com.alligator.market.domain.provider.exception.ProviderCodeDuplicateException;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Абстрактный сканер контекста приложения, содержащий доменную проверку на уникальность кода провайдера.
 */
public abstract non-sealed class AbstractProviderContextScanner implements ProviderContextScanner {

    /** Вернуть последовательность провайдеров для построения карты дескрипторов. */
    protected abstract Iterable<MarketDataProvider> providers();

    @Override
    public final Map<String, ProviderDescriptor> providerDescriptors() {
        Map<String, ProviderDescriptor> descriptors = new LinkedHashMap<>();
        for (MarketDataProvider provider : providers()) {
            String providerCode = provider.providerCode();
            ProviderDescriptor descriptor = provider.descriptor();
            ProviderDescriptor previous = descriptors.put(providerCode, descriptor);
            if (previous != null) {
                throw new ProviderCodeDuplicateException(providerCode);
            }
        }
        return descriptors;
    }
}
