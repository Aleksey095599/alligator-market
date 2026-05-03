package com.alligator.market.domain.marketdata.tick.level.capture.vo;

import java.util.Objects;

/**
 * Код потока сбора котировок.
 *
 * <p>Примеры: TWAP_FOREX_SPOT_CNYRUB_TOM_1M, PRICE_MONITOR_CNYRUB_TOM_5S.</p>
 *
 * @param value непустой код потока сбора котировок
 */
public record QuoteCollectionStreamCode(String value) {

    public QuoteCollectionStreamCode {
        Objects.requireNonNull(value, "value must not be null");

        if (value.isBlank()) {
            throw new IllegalArgumentException("value must not be blank");
        }
    }

    public static QuoteCollectionStreamCode of(String value) {
        return new QuoteCollectionStreamCode(value);
    }
}
