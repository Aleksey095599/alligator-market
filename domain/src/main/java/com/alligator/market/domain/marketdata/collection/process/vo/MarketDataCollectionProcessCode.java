package com.alligator.market.domain.marketdata.collection.process.vo;

import java.util.Locale;
import java.util.Objects;
import java.util.regex.Pattern;

/**
 * Объект-значение кода процесса сбора рыночных данных.
 *
 * <p>Примеры: TWAP_FOREX_SPOT_CNYRUB_TOM_1M, PRICE_MONITOR_CNYRUB_TOM_5S.</p>
 *
 * @param value нормализованный код процесса сбора
 */
public record MarketDataCollectionProcessCode(String value) {

    public static final String PATTERN = "^[A-Z0-9_.-]+$";
    private static final int MAX_LENGTH = 128;
    private static final Pattern VALIDATION_PATTERN = Pattern.compile(PATTERN);

    /**
     * Конструктор.
     */
    public MarketDataCollectionProcessCode {
        value = normalize(value);
    }

    /**
     * Фабрика для создания объекта из строкового кода.
     */
    public static MarketDataCollectionProcessCode of(String raw) {
        return new MarketDataCollectionProcessCode(raw);
    }

    private static String normalize(String raw) {
        Objects.requireNonNull(raw, "collectionProcessCode must not be null");

        String normalized = raw.strip();
        if (normalized.isEmpty()) {
            throw new IllegalArgumentException("collectionProcessCode must not be blank");
        }

        normalized = normalized.toUpperCase(Locale.ROOT);

        if (normalized.length() > MAX_LENGTH) {
            throw new IllegalArgumentException("collectionProcessCode length must be <= " + MAX_LENGTH);
        }

        if (!VALIDATION_PATTERN.matcher(normalized).matches()) {
            throw new IllegalArgumentException(
                    "collectionProcessCode must match pattern [A-Z0-9_.-]+: '" + normalized + "'"
            );
        }

        return normalized;
    }
}
