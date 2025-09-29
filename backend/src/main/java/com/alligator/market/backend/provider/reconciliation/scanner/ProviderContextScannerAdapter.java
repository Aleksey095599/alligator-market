package com.alligator.market.backend.provider.reconciliation.scanner;

import com.alligator.market.domain.provider.contract.MarketDataProvider;
import com.alligator.market.domain.provider.contract.descriptor.ProviderDescriptor;
import com.alligator.market.domain.provider.exception.ProviderCodeDuplicateException;
import com.alligator.market.domain.provider.exception.ProviderDescriptorDuplicateException;
import com.alligator.market.domain.provider.reconciliation.ProviderContextScanner;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Адаптер доменного сканера контекста приложения для получения данных о провайдерах рыночных данных.
 */
@Component
@RequiredArgsConstructor
public class ProviderContextScannerAdapter implements ProviderContextScanner {

    private final List<MarketDataProvider> providers;

    @Override
    public Map<String, ProviderDescriptor> providerDescriptors() {
        Map<String, ProviderDescriptor> descriptors = new LinkedHashMap<>();
        for (MarketDataProvider p : providers) {
            String providerCode = p.providerCode();
            ProviderDescriptor descriptor = p.descriptor();
            ProviderDescriptor previous = descriptors.put(providerCode, descriptor);
            if (previous != null) {
                throw new ProviderCodeDuplicateException(providerCode);
            }
        }
        return descriptors;
    }
}
