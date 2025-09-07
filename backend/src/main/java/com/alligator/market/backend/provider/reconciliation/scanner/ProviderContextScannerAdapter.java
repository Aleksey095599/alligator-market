package com.alligator.market.backend.provider.reconciliation.scanner;

import com.alligator.market.domain.provider.contract.InstrumentHandler;
import com.alligator.market.domain.provider.contract.MarketDataProvider;
import com.alligator.market.domain.provider.profile.model.Profile;
import com.alligator.market.domain.provider.reconciliation.ProviderContextScanner;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Компонент реализует доменный контракт сканера провайдеров в контексте.
 */
@Component
@RequiredArgsConstructor
public class ProviderContextScannerAdapter implements ProviderContextScanner {

    private final List<MarketDataProvider> providers;

    @Override
    public List<Profile> getProfiles() {
        return providers.stream()
                .map(MarketDataProvider::getProfile)
                .collect(Collectors.toList());
    }

    @Override
    public List<InstrumentHandler<? extends MarketDataProvider>> getHandlers() {
        return providers.stream()
                .flatMap(p -> p.getHandlers().values().stream())
                .collect(Collectors.toList());
    }
}
