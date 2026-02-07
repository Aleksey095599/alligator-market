package com.alligator.market.backend.provider.adapter.moex.iss.properties;

import jakarta.validation.constraints.NotBlank;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

/**
 * Параметры подключения к провайдеру рыночных данных MOEX ISS.
 *
 * <p>Параметры автоматически считываются из файла настроек приложения.</p>
 */
@Validated
@ConfigurationProperties("provider.connection-config.moex.iss")
public record MoexIssConnectionProperties(@NotBlank String baseUrl) {
}
