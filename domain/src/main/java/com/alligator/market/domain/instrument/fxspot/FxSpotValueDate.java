package com.alligator.market.domain.instrument.fxspot;

/**
 * Обозначения для даты расчетов инструмента {@link FxSpot}.
 */
public enum FxSpotValueDate {

    // Не задана
    VALUE_DATE_UNKNOWN,

    // Сегодня
    TOD,

    // Завтра
    TOM,

    // Послезавтра
    SPOT,

    // Раздельно
    SPLIT,
}
