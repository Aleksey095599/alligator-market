package com.alligator.market.backend.provider.maintenance.context.scanner;

import com.alligator.market.domain.provider.model.MarketDataProvider;
import com.alligator.market.domain.provider.maintenance.context.scanner.AbstractProviderContextScanner;

import java.util.List;
import java.util.Objects;

/**
 * Адаптер доменного {@link AbstractProviderContextScanner}.
 */
public class ProviderContextScannerAdapter extends AbstractProviderContextScanner {

    /* Список провайдеров. */
    private final List<MarketDataProvider> providers;

    /* Конструктор. */
    public ProviderContextScannerAdapter(List<MarketDataProvider> providers) {
        this.providers = Objects.requireNonNull(providers, "providers must not be null");
    }

    @Override
    protected Iterable<MarketDataProvider> providers() {
        return providers;
    }
}
