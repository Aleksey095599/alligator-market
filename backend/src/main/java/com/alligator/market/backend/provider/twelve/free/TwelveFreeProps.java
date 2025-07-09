package com.alligator.market.backend.provider.twelve.free;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConfigurationProperty;

/**
 * Параметры подключения к провайдеру TwelveData (free plan).
 */
@ConfigurationProperties("provider.connection-config.twelve-free")
public record TwelveFreeProps (

        @ConfigurationProperty("base-url")
        String baseUrl,

        @ConfigurationProperty("api-key")
        String apiKey
) {}
