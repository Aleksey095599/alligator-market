package com.alligator.market.domain.marketdata.tick.level.source.vo;

import java.util.Objects;

/**
 * Идентификатор инструмента в том виде, в котором он приходит из внешнего
 * источника рыночных данных, до сопоставления с внутренним кодом инструмента
 * приложения.
 *
 * @param value код инструмента в системе источника рыночных данных
 */
public record SourceInstrumentCode(
        String value
) {

    private static final int MAX_LENGTH = 128;

    public SourceInstrumentCode {
        value = normalize(value);
    }

    public static SourceInstrumentCode of(String raw) {
        return new SourceInstrumentCode(raw);
    }

    private static String normalize(String raw) {
        Objects.requireNonNull(raw, "sourceInstrumentCode must not be null");

        String normalized = raw.strip();
        if (normalized.isEmpty()) {
            throw new IllegalArgumentException("sourceInstrumentCode must not be blank");
        }

        if (normalized.length() > MAX_LENGTH) {
            throw new IllegalArgumentException("sourceInstrumentCode length must be <= " + MAX_LENGTH);
        }

        if (containsControlCharacter(normalized)) {
            throw new IllegalArgumentException("sourceInstrumentCode must not contain control characters");
        }

        return normalized;
    }

    private static boolean containsControlCharacter(String value) {
        return value.chars().anyMatch(Character::isISOControl);
    }
}
