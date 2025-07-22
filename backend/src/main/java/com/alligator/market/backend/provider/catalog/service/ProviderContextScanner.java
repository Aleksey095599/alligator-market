package com.alligator.market.backend.provider.catalog.service;

import com.alligator.market.domain.provider.MarketDataProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class ProviderContextScanner {

    private final List<MarketDataProvider> providers;

    public List<MarketDataProvider> findAll() {
        return List.copyOf(providers);
    }
}
