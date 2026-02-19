package com.alligator.market.backend.infra.time.prop;

import jakarta.validation.constraints.NotBlank;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

/**
 * Настройки времени приложения.
 *
 * <p>Данные параметры автоматически считываются из файла настроек приложения.</p>
 */
@Validated
@ConfigurationProperties("app.time")
public record AppTimeProperties(
        @NotBlank String businessZoneId
) {
}
