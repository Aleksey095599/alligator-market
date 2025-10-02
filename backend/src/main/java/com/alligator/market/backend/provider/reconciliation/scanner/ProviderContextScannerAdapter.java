package com.alligator.market.backend.provider.reconciliation.scanner;

import com.alligator.market.domain.provider.contract.MarketDataProvider;
import com.alligator.market.domain.provider.reconciliation.AbstractProviderContextScanner;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Адаптер доменного сканера контекста приложения для получения данных о провайдерах рыночных данных.
 */
@Component
@RequiredArgsConstructor
public class ProviderContextScannerAdapter extends AbstractProviderContextScanner {

    private final List<MarketDataProvider> providers;

    @Override
    protected Iterable<MarketDataProvider> providers() {
        return providers;
    }
}
