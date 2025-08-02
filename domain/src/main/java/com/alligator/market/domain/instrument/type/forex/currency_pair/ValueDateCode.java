package com.alligator.market.domain.instrument.type.forex.currency_pair;

/**
 * Краткие обозначения (коды) для value date конверсионных сделок.
 */
public enum ValueDateCode {

    // Провайдер не возвращает
    NONE,

    // Сегодня
    TOD,

    // Завтра
    TOM,

    // Послезавтра
    SPOT,

    // Форвард
    FWD,

    // Раздельный расчет
    SPLIT
}
