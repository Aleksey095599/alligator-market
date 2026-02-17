package com.alligator.market.domain.provider.maintenance.scanner.context;

import com.alligator.market.domain.provider.model.vo.ProviderCode;
import com.alligator.market.domain.provider.model.MarketDataProvider;
import com.alligator.market.domain.provider.model.passport.ProviderPassport;
import com.alligator.market.domain.provider.exception.ProviderCodeDuplicateException;
import com.alligator.market.domain.provider.exception.ProviderDisplayNameDuplicateException;

import java.util.*;

/**
 * Абстрактная реализация {@link ProviderContextScanner}.
 */
public abstract non-sealed class AbstractProviderContextScanner implements ProviderContextScanner {

    /**
     * Возвращает последовательность провайдеров из контекста приложения.
     */
    protected abstract Iterable<MarketDataProvider> providers();

    /**
     * Возвращает из контекста неизменяемую карту "код провайдера → паспорт провайдера" и валидирует инварианты:
     * <ul>
     *     <li>Коды провайдеров уникальны;</li>
     *     <li>Имена провайдеров уникальны.</li>
     * </ul>
     */
    @Override
    public final Map<ProviderCode, ProviderPassport> passportsByCode() {
        Map<ProviderCode, ProviderPassport> map = new LinkedHashMap<>();
        Set<String> displayNames = new HashSet<>();

        // Перебираем последовательность провайдеров
        for (MarketDataProvider provider : providers()) {
            Objects.requireNonNull(provider, "provider must not be null");

            // Извлекаем текущий код и паспорт
            ProviderCode code = Objects.requireNonNull(provider.providerCode(),
                    "providerCode must not be null");
            ProviderPassport passport = Objects.requireNonNull(provider.passport(),
                    "passport must not be null");

            // Добавляем текущий код и паспорт в карту, проверяя дублирование по коду
            ProviderPassport prev = map.put(code, passport);
            if (prev != null) {
                throw new ProviderCodeDuplicateException(code);
            }

            // Проверка дублирования по имени провайдера (без учёта регистра)
            String nameLower = passport.displayName().toLowerCase(Locale.ROOT);
            if (!displayNames.add(nameLower)) {
                throw new ProviderDisplayNameDuplicateException(passport.displayName());
            }
        }

        return java.util.Collections.unmodifiableMap(map);
    }
}
