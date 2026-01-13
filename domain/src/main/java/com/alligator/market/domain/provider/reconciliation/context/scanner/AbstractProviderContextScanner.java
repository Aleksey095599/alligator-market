package com.alligator.market.domain.provider.reconciliation.context.scanner;

import com.alligator.market.domain.provider.code.ProviderCode;
import com.alligator.market.domain.provider.contract.MarketDataProvider;
import com.alligator.market.domain.provider.contract.passport.ProviderPassport;
import com.alligator.market.domain.provider.exception.ProviderCodeDuplicateException;
import com.alligator.market.domain.provider.exception.ProviderDisplayNameDuplicateException;

import java.util.*;

/**
 * Абстрактный каркас, реализующий интерфейс сканера контекста приложения {@link ProviderContextScanner}.
 */
public abstract non-sealed class AbstractProviderContextScanner implements ProviderContextScanner {

    /**
     * Возвращает последовательность провайдеров для построения карты паспортов.
     */
    protected abstract Iterable<MarketDataProvider> providers();

    /**
     * Возвращает карту паспортов провайдеров {@link ProviderPassport}, индексированную по коду.
     */
    @Override
    public final Map<ProviderCode, ProviderPassport> providerPassports() {
        // Создаем пустую карту
        Map<ProviderCode, ProviderPassport> map = new LinkedHashMap<>();
        // Создаем пустой сет для проверки дублей по имени провайдера
        Set<String> displayNames = new HashSet<>();

        // Перебираем последовательность провайдеров
        for (MarketDataProvider provider : providers()) {
            // Извлекаем текущий код и паспорт
            ProviderCode code = provider.providerCode();
            ProviderPassport passport = provider.passport();

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
        return map;
    }
}
