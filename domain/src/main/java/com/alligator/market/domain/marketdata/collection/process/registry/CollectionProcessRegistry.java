package com.alligator.market.domain.marketdata.collection.process.registry;

import com.alligator.market.domain.marketdata.collection.process.MarketDataCollectionProcess;
import com.alligator.market.domain.marketdata.collection.process.passport.CollectionProcessPassport;
import com.alligator.market.domain.marketdata.collection.process.vo.MarketDataCollectionProcessCode;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Реестр процессов сбора рыночных данных, зарегистрированных в приложении.
 *
 * <p>Назначение: единый источник истины о доступных {@link MarketDataCollectionProcess}
 * и точка валидации их консистентности.</p>
 *
 * <p>Типичный жизненный цикл: реестр формируется при старте приложения и далее используется
 * как неизменяемый snapshot.</p>
 */
public interface CollectionProcessRegistry {

    /**
     * Неизменяемая карта "код процесса сбора → процесс сбора".
     */
    Map<MarketDataCollectionProcessCode, MarketDataCollectionProcess> processesByCode();

    /**
     * Производная проекция: неизменяемая карта "код процесса сбора → паспорт процесса сбора".
     */
    default Map<MarketDataCollectionProcessCode, CollectionProcessPassport> passportsByCode() {
        Map<MarketDataCollectionProcessCode, CollectionProcessPassport> map = new LinkedHashMap<>();

        for (Map.Entry<MarketDataCollectionProcessCode, MarketDataCollectionProcess> entry
                : processesByCode().entrySet()) {
            MarketDataCollectionProcessCode code = entry.getKey();
            MarketDataCollectionProcess process = entry.getValue();

            CollectionProcessPassport passport = Objects.requireNonNull(process.passport(),
                    "process.passport must not be null");

            map.put(code, passport);
        }

        return Collections.unmodifiableMap(map);
    }
}
