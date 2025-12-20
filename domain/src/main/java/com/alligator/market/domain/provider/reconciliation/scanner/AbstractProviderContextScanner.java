package com.alligator.market.domain.provider.reconciliation.scanner;

import com.alligator.market.domain.provider.contract.MarketDataProvider;
import com.alligator.market.domain.provider.exception.ProviderCodeDuplicateException;
import com.alligator.market.domain.provider.exception.ProviderDisplayNameDuplicateException;
import com.alligator.market.domain.provider.reconciliation.dto.ProviderSnapshot;

import java.util.*;

/**
 * Абстрактный каркас для контракта сканера контекста приложения {@link ProviderContextScanner}.
 */
public abstract non-sealed class AbstractProviderContextScanner implements ProviderContextScanner {

    /**
     * Вернуть последовательность провайдеров для построения карты дескрипторов.
     */
    protected abstract Iterable<MarketDataProvider> providers();

    /**
     * Вернуть карту снимков провайдеров {@link ProviderSnapshot}, индексированную по коду.
     * Содержит проверки на дублирование по коду провайдера и отображаемому имени.
     */
    @Override
    public final Map<String, ProviderSnapshot> providerSnapshots() {
        Map<String, ProviderSnapshot> snapshots = new LinkedHashMap<>();
        Set<String> displayNamesLower = new HashSet<>();

        for (MarketDataProvider provider : providers()) {
            String code = provider.providerCode();
            var descriptor = provider.descriptor();
            var policy = provider.policy();

            // Проверка дублей по коду
            ProviderSnapshot prev = snapshots.put(code, new ProviderSnapshot(code, descriptor, policy));
            if (prev != null) {
                throw new ProviderCodeDuplicateException(code);
            }

            // Проверка дублей по отображаемому имени (без учёта регистра)
            String nameLower = descriptor.displayName().toLowerCase(Locale.ROOT);
            if (!displayNamesLower.add(nameLower)) {
                throw new ProviderDisplayNameDuplicateException(descriptor.displayName());
            }
        }
        return snapshots;
    }
}
