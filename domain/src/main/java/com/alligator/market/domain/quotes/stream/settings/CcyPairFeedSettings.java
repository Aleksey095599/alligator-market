package com.alligator.market.domain.quotes.stream.settings;

/**
 * Настройки источника котировок для заданной валютной пары.
 */
public record CcyPairFeedSettings(

        String pair,
        String provider,
        short priority,
        int refreshMs, // 0 для PUSH
        boolean enabled

) {
}



