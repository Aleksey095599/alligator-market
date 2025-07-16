package com.alligator.market.domain.instrument;

/**
 * Список базовых типов финансовых инструментов.
 */
public enum InstrumentType {

    // Валютная пара на рынке Forex
    CURRENCY_PAIR,

    // Акция компании
    STOCK,

    // Долговая ценная бумага
    BOND,

    // Фондовый индекс
    INDEX,

    // Национальная валюта
    CURRENCY
}
