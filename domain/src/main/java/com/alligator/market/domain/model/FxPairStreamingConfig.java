package com.alligator.market.domain.model;

/** Конфигурация стрима котировок для заданной валютной пары. */
public record FxPairStreamingConfig(

        String pair,
        String provider,
        short priority,
        int refreshMs,
        boolean enabled

) {}



