package com.alligator.market.domain.instrument.type.forex.currency_pair;

/**
 * Типы расчетов для валютных операций.
 */
public enum SettlementType {

    // Сегодня
    TOD,

    // Завтра
    TOM,

    // Спот
    SPOT,

    // Раздельный расчет
    SPLIT,

    // Форвард
    FWD
}
