package com.alligator.market.backend.provider.maintenance.context.scanner;

import com.alligator.market.domain.provider.model.MarketDataProvider;
import com.alligator.market.domain.provider.maintenance.context.scanner.AbstractProviderContextScanner;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Адаптер доменного сканера контекста приложения в среде Spring.
 */
@Component
@RequiredArgsConstructor
public class ProviderContextScannerAdapter extends AbstractProviderContextScanner {

    /* Список провайдеров. */
    private final List<MarketDataProvider> providers;

    @Override
    protected Iterable<MarketDataProvider> providers() {
        return providers;
    }
}
