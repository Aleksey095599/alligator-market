package com.alligator.market.backend.provider.reconciliation.scanner;

import com.alligator.market.domain.provider.contract.MarketDataProvider;
import com.alligator.market.domain.provider.contract.descriptor.ProviderDescriptor;
import com.alligator.market.domain.provider.reconciliation.ProviderContextScanner;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Адаптер доменного контракта сканера контекста приложения касательно провайдеров рыночных данных.
 */
@Component
@RequiredArgsConstructor
public class ProviderContextScannerAdapter implements ProviderContextScanner {

    private final List<MarketDataProvider> providers;

    @Override
    public List<ProviderDescriptor> providerDescriptors() {
        return providers.stream()
                .map(MarketDataProvider::descriptor)
                .collect(Collectors.toList());
    }
}
