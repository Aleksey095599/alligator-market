package com.alligator.market.backend.config.time;

import jakarta.validation.constraints.NotBlank;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

/**
 * Настройка временной зоны.
 * Автоматически считывается из настроек приложения.
 */
@Validated
@ConfigurationProperties("app")
public record AppTimeProps(

        @NotBlank
        String timeZone
) {}
