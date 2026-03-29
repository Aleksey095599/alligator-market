package com.alligator.market.backend.infra.time.prop;

import jakarta.validation.constraints.NotBlank;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

/* Настройки времени приложения. */
@Validated
@ConfigurationProperties("app.time")
public record AppTimeProperties(
        /* Бизнесовая временная зона приложения. */
        @NotBlank String businessZoneId
) {
}
