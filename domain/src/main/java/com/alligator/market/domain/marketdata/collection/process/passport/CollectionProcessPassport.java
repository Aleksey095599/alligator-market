package com.alligator.market.domain.marketdata.collection.process.passport;

import com.alligator.market.domain.marketdata.collection.process.vo.MarketDataCollectionProcessDisplayName;

import java.util.Objects;

/**
 * Паспорт процесса сбора рыночных данных: иммутабельные метаданные для витрины.
 *
 * <p>Назначение: отображение в каталоге, UI/API, логах и диагностике.</p>
 *
 * @param displayName отображаемое имя процесса сбора
 */
public record CollectionProcessPassport(
        MarketDataCollectionProcessDisplayName displayName
) {

    public CollectionProcessPassport {
        Objects.requireNonNull(displayName, "displayName must not be null");
    }
}
