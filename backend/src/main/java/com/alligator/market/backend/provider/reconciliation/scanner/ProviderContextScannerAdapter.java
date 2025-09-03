package com.alligator.market.backend.provider.reconciliation.scanner;

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

    /**
     * Возвращает карту профилей из контекста,
     * где первый ключ — технический код провайдера, второй ключ — отображаемое имя провайдера.
     */
    @Override
    public Map<String, Profile> getProfiles() {

        // Извлекаем профили
        List<Profile> profiles = providers.stream()
                .map(MarketDataProvider::getProfile)
                .toList();

        // Возвращаем Map по коду провайдера
        return profiles.stream()
                .collect(Collectors.toMap(Profile::providerCode, Function.identity()));
    }

    /**
     * Возвращает обработчики (handlers) финансовых инструментов,
     * где первый ключ — код провайдера, второй ключ — тип инструмента.
     */
    @Override
    public Map<String, Map<String, InstrumentHandler>> getHandlers() {

        // Возвращаем Map по коду провайдера и типу инструмента
        return providers.stream()
                .collect(Collectors.toMap(
                        p -> p.getProfile().providerCode(),
                        p -> p.getHandlers().stream()
                                .collect(Collectors.toMap(
                                        h -> h.getSupportedInstrumentType().name(),
                                        Function.identity()
                                ))
                ));
    }
}
