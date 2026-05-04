package com.alligator.market.domain.provider.registry;

import com.alligator.market.domain.provider.MarketDataProvider;
import com.alligator.market.domain.provider.passport.ProviderPassport;
import com.alligator.market.domain.provider.vo.ProviderCode;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Реестр провайдеров рыночных данных, зарегистрированных в приложении.
 *
 * <p>Назначение: единый источник истины о доступных {@link MarketDataProvider} в рамках приложения
 * и точка валидации их консистентности.</p>
 *
 * <p>Типичный жизненный цикл: реестр формируется при старте приложения и далее используется
 * как неизменяемый snapshot.</p>
 */
public interface ProviderRegistry {

    /**
     * Неизменяемая карта "код провайдера → провайдер".
     */
    Map<ProviderCode, MarketDataProvider> providersByCode();

    /**
     * Производная проекция: неизменяемая карта "код провайдера → паспорт провайдера".
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
