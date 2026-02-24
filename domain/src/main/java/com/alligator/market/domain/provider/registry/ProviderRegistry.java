package com.alligator.market.domain.provider.registry;

import com.alligator.market.domain.provider.model.MarketDataProvider;
import com.alligator.market.domain.provider.model.passport.ProviderPassport;
import com.alligator.market.domain.provider.model.vo.ProviderCode;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Реестр провайдеров рыночных данных, зарегистрированных в приложении.
 *
 * <p><b>Назначение:</b> единый источник истины о доступных {@link MarketDataProvider} в рамках приложения
 * и точка валидации их консистентности.</p>
 *
 * <p><b>Типичный жизненный цикл</b>: реестр формируется при старте приложения и далее используется
 * как неизменяемый snapshot.</p>
 *
 * <p><b>Инварианты и требования, обеспечивающие консистентность данных реестра:</b></p>
 * <ul>
 *     <li>Для каждого провайдера {@link MarketDataProvider#providerCode()} не null и уникален;</li>
 *     <li>Для каждого провайдера {@link MarketDataProvider#passport()} не null;</li>
 *     <li>Для каждого провайдера {@link MarketDataProvider#policy()} не null;</li>
 *     <li>Для каждого провайдера {@link ProviderPassport#displayName()} не null и уникален без учёта регистра;</li>
 *     <li>Реестр должен содержать не менее одного провайдера.</li>
 * </ul>
 */
public interface ProviderRegistry {

    /**
     * Неизменяемая карта "код провайдера → провайдер".
     */
    Map<ProviderCode, MarketDataProvider> providersByCode();

    /**
     * Производная проекция: неизменяемая карта "код провайдера → паспорт провайдера".
     *
     * <p>Дефолтная реализация строит карту на основе {@link #providersByCode()} и пересоздаёт её
     * при каждом вызове. Snapshot-реализации могут переопределить метод и возвращать заранее
     * подготовленную неизменяемую карту.</p>
     */
    default Map<ProviderCode, ProviderPassport> passportsByCode() {
        Map<ProviderCode, ProviderPassport> map = new LinkedHashMap<>();

        for (Map.Entry<ProviderCode, MarketDataProvider> entry : providersByCode().entrySet()) {
            ProviderCode code = entry.getKey();
            MarketDataProvider provider = entry.getValue();

            ProviderPassport passport = Objects.requireNonNull(provider.passport(),
                    "provider.passport must not be null");

            map.put(code, passport);
        }

        return Collections.unmodifiableMap(map);
    }
}
