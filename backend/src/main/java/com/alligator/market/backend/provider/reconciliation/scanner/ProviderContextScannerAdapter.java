package com.alligator.market.backend.provider.reconciliation.scanner;

import com.alligator.market.domain.provider.contract.MarketDataProvider;
import com.alligator.market.domain.provider.reconciliation.scaner.AbstractProviderContextScanner;
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

    /**
     * Переназначаем метод {@link AbstractProviderContextScanner#providers()} из абстрактного доменного сканера.
     * Остальные методы заданы в абстрактном доменном сканере.
     */
    @Override
    protected Iterable<MarketDataProvider> providers() {
        return providers;
    }
}
