package com.alligator.market.backend.config.time;

import jakarta.validation.constraints.NotBlank;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

/**
 * Настройки временной зоны.
 */
@Validated
@ConfigurationProperties(prefix = "app")
public record AppTimeProps(

        @NotBlank
        @ValidZoneId
        String timeZone
) {}
