package com.alligator.market.backend.provider.adapter.moex.iss.config;

import jakarta.validation.constraints.NotBlank;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

/**
 * Параметры подключения к провайдеру рыночных данных MOEX ISS.
 * Автоматически считываются из настроек приложения.
 */
@Validated
@ConfigurationProperties("provider.connection-config.moex.iss")
public record MoexIssAdapterProps(
        @NotBlank
        String baseUrl
) {
}
