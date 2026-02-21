package com.alligator.market.domain.provider.registry;

import com.alligator.market.domain.provider.exception.ProviderCodeDuplicateException;
import com.alligator.market.domain.provider.exception.ProviderDisplayNameDuplicateException;
import com.alligator.market.domain.provider.model.MarketDataProvider;
import com.alligator.market.domain.provider.model.passport.ProviderPassport;
import com.alligator.market.domain.provider.model.vo.ProviderCode;

import java.util.*;

/**
 * Абстрактная реализация {@link ProviderRegistry}.
 *
 * <p>Логика построения:</p>
 * <ul>
 *     <li>Задается источник провайдеров как последовательность доменных {@link MarketDataProvider};</li>
 *     <li>Родительский карта {@link ProviderRegistry#providersByCode()} строится из заданной последовательности
 *     путем перебора провайдеров.</li>
 *     <li>В процессе перебора проверяются инварианты родительского контракта.</li>
 * </ul>
 */
public abstract non-sealed class AbstractProviderRegistry implements ProviderRegistry {

    //=================================================================================================================
    // ТОЧКИ РАСШИРЕНИЯ ДЛЯ НАСЛЕДНИКОВ
    //=================================================================================================================

    /**
     * Источник провайдеров для построения доменного реестра.
     */
    protected abstract Iterable<MarketDataProvider> providers();

    //=================================================================================================================
    // РЕАЛИЗАЦИЯ МЕТОДОВ КОНТРАКТА
    //=================================================================================================================

    /**
     * Возвращает неизменяемую карту "код провайдера → провайдер" и валидирует инварианты родительского контракта:
     * <ul>
     *     <li>Коды провайдеров уникальны;</li>
     *     <li>Имена провайдеров уникальны (без учёта регистра);</li>
     *     <li>Паспорт провайдера не null.</li>
     * </ul>
     */
    @Override
    public final Map<ProviderCode, MarketDataProvider> providersByCode() {
        Map<ProviderCode, MarketDataProvider> map = new LinkedHashMap<>();
        Set<String> displayNames = new HashSet<>();

        // Перебираем последовательность провайдеров
        for (MarketDataProvider provider : providers()) {
            Objects.requireNonNull(provider, "provider must not be null");

            // Извлекаем текущий код и паспорт
            ProviderCode code = Objects.requireNonNull(provider.providerCode(),
                    "providerCode must not be null");
            ProviderPassport passport = Objects.requireNonNull(provider.passport(),
                    "passport must not be null");

            // Добавляем провайдера в карту, проверяя дублирование по коду
            MarketDataProvider prev = map.put(code, provider);
            if (prev != null) {
                throw new ProviderCodeDuplicateException(code);
            }

            // Проверка дублирования по имени провайдера (без учёта регистра)
            String displayName = Objects.requireNonNull(passport.displayName(),
                    "passport.displayName must not be null");
            String nameLower = displayName.toLowerCase(Locale.ROOT);

            if (!displayNames.add(nameLower)) {
                throw new ProviderDisplayNameDuplicateException(displayName);
            }
        }

        return Collections.unmodifiableMap(map);
    }
}

