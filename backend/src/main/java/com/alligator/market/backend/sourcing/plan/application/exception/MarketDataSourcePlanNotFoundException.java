package com.alligator.market.backend.sourcing.plan.application.exception;

import com.alligator.market.domain.instrument.vo.InstrumentCode;
import com.alligator.market.domain.marketdata.collection.process.vo.MarketDataCollectionProcessCode;

import java.util.Objects;

/**
 * Ошибка application-слоя: план источников для инструмента не найден.
 */
public final class MarketDataSourcePlanNotFoundException extends IllegalStateException {

    public MarketDataSourcePlanNotFoundException(
            MarketDataCollectionProcessCode collectionProcessCode,
            InstrumentCode instrumentCode
    ) {
        super("Market data source plan for collection process '" +
                Objects.requireNonNull(collectionProcessCode, "collectionProcessCode must not be null").value() +
                "' and instrument '" +
                Objects.requireNonNull(instrumentCode, "instrumentCode must not be null").value() +
                "' does not exist");
    }
}
