package com.alligator.market.domain.provider.registry;

import com.alligator.market.domain.provider.model.MarketDataProvider;
import com.alligator.market.domain.provider.model.passport.ProviderPassport;
import com.alligator.market.domain.provider.model.vo.ProviderCode;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Реестр действующих провайдеров рыночных данных.
 *
 * <p>Назначение: Источник истины по составу действующих провайдеров внутри приложения.</p>
 *
 * <p>Инварианты контракта:</p>
 * <ul>
 *     <li>Коды провайдеров уникальны;</li>
 *     <li>Имена провайдеров (displayName в паспорте) уникальны без учёта регистра;</li>
 *     <li>Паспорт провайдера не null.</li>
 * </ul>
 */
public sealed interface ProviderRegistry permits AbstractProviderRegistry {

    /**
     * Возвращает неизменяемую карту "код провайдера → провайдер".
     */
    Map<ProviderCode, MarketDataProvider> providersByCode();

    /**
     * Возвращает неизменяемую карту "код провайдера → паспорт провайдера".
     *
     * <p>Примечание: Паспорта извлекаются из {@link #providersByCode()}.</p>
     */
    default Map<ProviderCode, ProviderPassport> passportsByCode() {
        Map<ProviderCode, ProviderPassport> map = new LinkedHashMap<>();

        for (Map.Entry<ProviderCode, MarketDataProvider> entry : providersByCode().entrySet()) {
            ProviderCode code = entry.getKey();
            MarketDataProvider provider = entry.getValue();

            ProviderPassport passport = Objects.requireNonNull(provider.passport(),
                    "passport must not be null");

            map.put(code, passport);
        }

        return Collections.unmodifiableMap(map);
    }
}
