package com.alligator.market.domain.marketdata.tick.level.capture.vo;

import java.util.Objects;

/**
 * Код процесса сбора рыночных данных.
 *
 * <p>Примеры: TWAP_FOREX_SPOT_CNYRUB_TOM_1M, PRICE_MONITOR_CNYRUB_TOM_5S.</p>
 *
 * @param value непустой код процесса сбора рыночных данных
 */
public record MarketDataCollectionProcessCode(String value) {

    public MarketDataCollectionProcessCode {
        Objects.requireNonNull(value, "value must not be null");

        if (value.isBlank()) {
            throw new IllegalArgumentException("value must not be blank");
        }
    }

    public static MarketDataCollectionProcessCode of(String value) {
        return new MarketDataCollectionProcessCode(value);
    }
}
