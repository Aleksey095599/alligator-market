package com.alligator.market.backend.config.time;

import jakarta.validation.constraints.NotNull;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import java.time.ZoneId;

/**
 * Настройка временной зоны.
 * Автоматически считывается из настроек приложения.
 */
@Validated
@ConfigurationProperties("app")
public record AppTimeProps(
        @NotNull
        ZoneId timeZone
) {}
