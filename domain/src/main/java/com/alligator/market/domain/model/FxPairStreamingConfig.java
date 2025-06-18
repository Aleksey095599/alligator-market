package com.alligator.market.domain.model;

/** Настройка потока котировок для конкретной пары и провайдера. */
public record FxPairStreamingConfig(

        String pair,
        String provider,
        short priority,
        int refreshMs,
        boolean enabled

) {}



