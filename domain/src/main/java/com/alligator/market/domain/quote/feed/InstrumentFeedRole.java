package com.alligator.market.domain.quote.feed;

/**
 * Роль источника котировок (feed) для финансового инструмента.
 *
 * <p>Используется в конфигурации инструмента для различения основного и резервного (альтернативного)
 * источника котировок.
 */
public enum InstrumentFeedRole {
    PRIMARY,
    SECONDARY
}
