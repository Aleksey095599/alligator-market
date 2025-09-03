package com.alligator.market.backend.provider.reconciliation.scanner;

import com.alligator.market.domain.instrument.type.InstrumentType;
import com.alligator.market.domain.provider.contract.MarketDataProvider;
import com.alligator.market.domain.provider.handler.contract.InstrumentHandler;
import com.alligator.market.domain.provider.profile.model.Profile;
import com.alligator.market.domain.provider.reconciliation.ProviderContextScanner;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Компонент реализует доменный контракт сканера контекста провайдеров.
 */
@Component
@RequiredArgsConstructor
public class ProviderContextScannerAdapter implements ProviderContextScanner {

    // Список всех адаптеров провайдеров
    private final List<MarketDataProvider> providers;

    @Override
    public Map<String, Map<String, Profile>> getProfiles() {
        return providers.stream()
                .map(MarketDataProvider::getProfile)
                .collect(Collectors.groupingBy(
                        Profile::providerCode,
                        Collectors.toMap(Profile::displayName, Function.identity())
                ));
    }

    @Override
    public Map<String, Map<InstrumentType, InstrumentHandler>> getHandlers() {
        return providers.stream()
                .collect(Collectors.toMap(
                        p -> p.getProfile().providerCode(),
                        p -> p.getHandlers().stream()
                                .collect(Collectors.toMap(
                                        InstrumentHandler::getSupportedInstrumentType,
                                        Function.identity()
                                ))
                ));
    }
}
