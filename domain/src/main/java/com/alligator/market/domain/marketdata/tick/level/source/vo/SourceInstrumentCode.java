package com.alligator.market.domain.marketdata.tick.level.source.vo;

import java.util.Objects;

/**
 * Идентификатор инструмента в том виде, в котором он приходит из внешнего
 * источника рыночных данных, до сопоставления с внутренним кодом инструмента
 * приложения.
 *
 * @param value код инструмента в системе источника рыночных данных
 */
public record SourceInstrumentCode(String value) {

    public SourceInstrumentCode {
        Objects.requireNonNull(value, "value must not be null");

        if (value.isBlank()) {
            throw new IllegalArgumentException("value must not be blank");
        }
    }

    /**
     * Создает код инструмента источника из его строкового значения.
     *
     * @param value непустой исходный код инструмента из системы источника
     * @return код инструмента источника
     */
    public static SourceInstrumentCode of(String value) {
        return new SourceInstrumentCode(value);
    }
}
