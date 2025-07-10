package com.alligator.market.backend.provider.twelve.free;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Параметры подключения к провайдеру TwelveData (free plan).
 */
@ConfigurationProperties("provider.connection-config.twelve-free")
public record TwelveFreeProps (

        String baseUrl,
        String apiKey
) {}
