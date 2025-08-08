package com.alligator.market.domain.instrument.fxspot;

/**
 * Обозначения для даты расчетов составных валют инструмента {@link FxSpot}.
 */
public enum CurrencyValueDate {

    // Не задана
    UNKNOWN,

    // Сегодня
    TOD,

    // Завтра
    TOM,

    // Послезавтра
    SPOT,
}
