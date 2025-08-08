package com.alligator.market.domain.instrument.currency_pair.model;

/**
 * Коды value date для конверсионных сделок.
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
