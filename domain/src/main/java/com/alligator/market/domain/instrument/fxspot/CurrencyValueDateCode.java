package com.alligator.market.domain.instrument.fxspot;

/**
 * Коды value date для конверсионных сделок.
 */
public enum CurrencyValueDateCode {

    // Не указано (встречается у некоторых провайдеров для индикативных котировок)
    UNKNOWN,

    // Сегодня
    TOD,

    // Завтра
    TOM,

    // Послезавтра
    SPOT,
}
