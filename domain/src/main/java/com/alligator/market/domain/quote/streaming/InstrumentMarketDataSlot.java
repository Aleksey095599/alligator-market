package com.alligator.market.domain.quote.streaming;

/**
 * Слоты источника котировок.
 *
 * <p>Используются в целях маркировки потока котировок как основной или дополнительный.
 */
public enum InstrumentMarketDataSlot {
    PRIMARY,  // <-- Основной источник котировок
    SECONDARY // <-- Дополнительный источник котировок
}
