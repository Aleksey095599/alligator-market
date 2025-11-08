package com.alligator.market.backend.provider.adapter.twelve.free.config;

import jakarta.validation.constraints.NotBlank;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

/**
 * Параметры подключения к провайдеру рыночных данных TwelveData (бесплатная подписка).
 * Автоматически считываются из настроек приложения.
 */
@Validated
@ConfigurationProperties("provider.connection-config.twelve-free")
public record TwelveFreeConnectionProps(
        @NotBlank String baseUrl,
        @NotBlank String apiKey
) {
}
