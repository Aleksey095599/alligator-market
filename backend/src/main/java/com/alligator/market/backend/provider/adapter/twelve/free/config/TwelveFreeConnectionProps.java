package com.alligator.market.backend.provider.adapter.twelve.free.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Параметры подключения к провайдеру рыночных данных (далее - провайдера) TwelveData (бесплатная подписка).
 * Автоматически считываются из настроек приложения.
 */
@ConfigurationProperties("provider.connection-config.twelve-free")
public record TwelveFreeConnectionProps(
        String baseUrl,
        String apiKey
) {}
