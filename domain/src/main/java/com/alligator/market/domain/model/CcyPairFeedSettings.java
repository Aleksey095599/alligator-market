package com.alligator.market.domain.model;

/** Настройки источника котировок для заданной валютной пары. */
public record CcyPairFeedSettings(

        String pair,
        String provider,
        short priority,
        int refreshMs,
        boolean enabled

) {}



