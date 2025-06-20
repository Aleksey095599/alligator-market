package com.alligator.market.domain.quote.stream.settings;

/**
 * Настройки источника котировок для заданной валютной пары.
 */
public record CcyPairFeedSettings(

        String pair,
        String provider,
        String mode,
        short priority,
        int refreshMs, // 0 для PUSH
        boolean enabled

) {
}



