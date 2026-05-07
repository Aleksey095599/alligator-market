package com.alligator.market.domain.source.registry;

import com.alligator.market.domain.source.MarketDataSource;
import com.alligator.market.domain.source.passport.ProviderPassport;
import com.alligator.market.domain.source.vo.ProviderCode;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Реестр провайдеров рыночных данных, зарегистрированных в приложении.
 *
 * <p>Назначение: единый источник истины о доступных {@link MarketDataSource} в рамках приложения
 * и точка валидации их консистентности.</p>
 *
 * <p>Типичный жизненный цикл: реестр формируется при старте приложения и далее используется
 * как неизменяемый snapshot.</p>
 */
public interface ProviderRegistry {

    /**
     * Неизменяемая карта "код провайдера → провайдер".
     */
    Map<ProviderCode, MarketDataSource> providersByCode();

    /**
     * Производная проекция: неизменяемая карта "код провайдера → паспорт провайдера".
     */
    default Map<ProviderCode, ProviderPassport> passportsByCode() {
        Map<ProviderCode, ProviderPassport> map = new LinkedHashMap<>();

        for (Map.Entry<ProviderCode, MarketDataSource> entry : providersByCode().entrySet()) {
            ProviderCode code = entry.getKey();
            MarketDataSource source = entry.getValue();

            ProviderPassport passport = Objects.requireNonNull(source.passport(),
                    "source.passport must not be null");

            map.put(code, passport);
        }

        return Collections.unmodifiableMap(map);
    }
}
