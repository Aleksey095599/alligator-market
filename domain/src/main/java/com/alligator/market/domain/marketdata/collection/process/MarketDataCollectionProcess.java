package com.alligator.market.domain.marketdata.collection.process;

import com.alligator.market.domain.instrument.vo.InstrumentCode;
import com.alligator.market.domain.marketdata.collection.process.passport.CollectionProcessPassport;
import com.alligator.market.domain.marketdata.collection.process.policy.CollectionProcessPolicy;
import com.alligator.market.domain.marketdata.collection.process.vo.MarketDataCollectionProcessCode;
import com.alligator.market.domain.marketdata.collection.process.vo.MarketDataCollectionProcessDisplayName;

import java.util.Objects;
import java.util.Set;

/**
 * Доменное описание процесса сбора рыночных данных.
 */
public interface MarketDataCollectionProcess {

    /**
     * Уникальный код процесса сбора рыночных данных.
     */
    MarketDataCollectionProcessCode processCode();

    /**
     * Паспорт процесса сбора рыночных данных.
     */
    CollectionProcessPassport passport();

    /**
     * Политика процесса сбора рыночных данных.
     */
    CollectionProcessPolicy policy();

    /**
     * Человекочитаемое имя процесса для администрирования и диагностики.
     */
    default MarketDataCollectionProcessDisplayName displayName() {
        return passport().displayName();
    }

    /**
     * Внутренние инструменты приложения, которые может обслуживать этот процесс.
     */
    default Set<InstrumentCode> supportedInstrumentCodes() {
        return policy().supportedInstrumentCodes();
    }

    /**
     * Проверяет, поддерживается ли инструмент этим процессом сбора.
     */
    default boolean supportsInstrument(InstrumentCode instrumentCode) {
        Objects.requireNonNull(instrumentCode, "instrumentCode must not be null");
        return supportedInstrumentCodes().contains(instrumentCode);
    }
}
