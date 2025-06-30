package com.alligator.market.domain.quotes;

/**
 * Настройки источника котировок для заданной валютной пары.
 */
public record CcyPairFeedSettings(

        String pair,
        String provider,
        short priority,
        int fetchPeriodMs, // 0 для PUSH
        boolean enabled

) {
}



