package com.alligator.market.backend.provider.adapter.moex.iss.config;

import jakarta.validation.constraints.NotBlank;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

/**
 * <b>Параметры подключения к провайдеру рыночных данных MOEX ISS.</b>
 *
 * <p>Данные параметры автоматически считываются из файла настроек приложения.</p>
 * <p>Spring создаёт бин этого типа, который может быть внедрён в другие компоненты.</p>
 */
@Validated
@ConfigurationProperties("provider.connection-config.moex.iss")
public record MoexIssAdapterProps(
        @NotBlank
        String baseUrl
) {
}
