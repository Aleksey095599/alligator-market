package com.alligator.market.backend.provider.twelve.free.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Параметры подключения к провайдеру TwelveData (free plan).
 * Считываются из настроек приложения.
 */
@ConfigurationProperties("provider.connection-config.twelve-free")
public record TwelveFreeProps(

        String baseUrl,
        String apiKey
) {}
