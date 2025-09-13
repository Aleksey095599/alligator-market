package com.alligator.market.backend.provider.reconciliation.scanner;

import com.alligator.market.domain.instrument.base.contract.Instrument;
import com.alligator.market.domain.provider.model.handler.InstrumentHandler;
import com.alligator.market.domain.provider.model.MarketDataProvider;
import com.alligator.market.domain.provider.model.info.ProviderStaticInfo;
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
    public List<ProviderStaticInfo> getProfiles() {
        return providers.stream()
                .map(MarketDataProvider::getProfile)
                .collect(Collectors.toList());
    }

    @Override
    /** Возвращает обработчики без повторов. */
    public List<InstrumentHandler<? extends MarketDataProvider, ? extends Instrument>> getHandlers() {
        return providers.stream()
                .flatMap(p -> p.getHandlers().values().stream().distinct())
                .collect(Collectors.toList());
    }
}
