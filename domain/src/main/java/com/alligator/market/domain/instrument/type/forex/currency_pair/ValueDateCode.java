package com.alligator.market.domain.instrument.type.forex.currency_pair;

/**
 * Краткие обозначения (коды) для value date конверсионных сделок.
 */
public enum ValueDateCode {

    // Индикативная котировка (провайдер не возвращает параметр value date)
    IND,

    // Сегодня
    TOD,

    // Завтра
    TOM,

    // Послезавтра
    SPOT,

    // Раздельный расчет
    SPLIT,

    // Форвард (>SPOT)
    FWD
}
