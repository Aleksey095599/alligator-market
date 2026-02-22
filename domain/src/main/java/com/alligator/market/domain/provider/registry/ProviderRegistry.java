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
 * <p>Назначение: Реестр используется как источник истины для получения данных о провайдерах,
 * зарегистрированных в приложении.</p>
 *
 * <p><b>Инварианты провайдеров в реестре:</b></p>
 * <ul>
 *     <li>Код провайдера {@link MarketDataProvider#providerCode()} не null и уникален;</li>
 *     <li>Паспорт провайдера {@link MarketDataProvider#passport()} не null;</li>
 *     <li>Политика провайдера {@link MarketDataProvider#policy()} не null;</li>
 *     <li>Имя провайдера в паспорте {@link ProviderPassport#displayName()} не null и уникально без учёта регистра.</li>
 * </ul>
 */
public interface ProviderRegistry {

    /**
     * Неизменяемая карта "код провайдера → провайдер".
     */
    Map<ProviderCode, MarketDataProvider> providersByCode();

    /**
     * Неизменяемая карта "код провайдера → паспорт провайдера".
     *
     * <p>Примечание: Дефолтная реализация может быть переопределена более эффективным способом.</p>
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
