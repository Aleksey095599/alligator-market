package com.alligator.market.backend.provider.adapter.moex.iss.instrument.forex.spot.properties;

import jakarta.validation.constraints.NotBlank;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

/**
 * Параметры подключения к провайдеру рыночных данных MOEX ISS по инструментам типа FOREX_SPOT.
 *
 * <p>Параметры автоматически считываются из файла настроек приложения.</p>
 */
@Validated
@ConfigurationProperties("provider.connection-properties.moex.iss.fx-spot")
public record MoexIssFxSpotConnectionProperties(@NotBlank String baseUrl) {
}
