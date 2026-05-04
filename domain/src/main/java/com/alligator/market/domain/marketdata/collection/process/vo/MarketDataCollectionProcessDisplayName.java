package com.alligator.market.domain.marketdata.collection.process.vo;

import java.util.Objects;

/**
 * Объект-значение отображаемого имени процесса сбора рыночных данных.
 *
 * @param value непустое имя процесса для интерфейса и диагностики
 */
public record MarketDataCollectionProcessDisplayName(String value) {

    private static final int MAX_LENGTH = 160;

    /**
     * Конструктор.
     */
    public MarketDataCollectionProcessDisplayName {
        value = normalize(value);
    }

    /**
     * Фабрика для создания объекта из строкового имени.
     */
    public static MarketDataCollectionProcessDisplayName of(String value) {
        return new MarketDataCollectionProcessDisplayName(value);
    }

    private static String normalize(String raw) {
        Objects.requireNonNull(raw, "displayName must not be null");

        String normalized = raw.strip();
        if (normalized.isEmpty()) {
            throw new IllegalArgumentException("displayName must not be blank");
        }

        if (normalized.length() > MAX_LENGTH) {
            throw new IllegalArgumentException("displayName length must be <= " + MAX_LENGTH);
        }

        if (containsControlCharacter(normalized)) {
            throw new IllegalArgumentException("displayName must not contain control characters");
        }

        return normalized;
    }

    private static boolean containsControlCharacter(String value) {
        return value.chars().anyMatch(Character::isISOControl);
    }
}
