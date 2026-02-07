package com.alligator.market.backend.provider.adapter.moex.iss.config.props;

import jakarta.validation.constraints.NotBlank;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

/**
 * Параметры подключения к провайдеру рыночных данных MOEX ISS.
 *
 * <p>Данные параметры автоматически считываются из файла настроек приложения. Spring создаёт бин этого типа,
 * который можно внедрять в другие компоненты.</p>
 */
@Validated
@ConfigurationProperties("provider.connection-config.moex.iss")
public record MoexIssAdapterProperties(
        @NotBlank
        String baseUrl
) {
}
