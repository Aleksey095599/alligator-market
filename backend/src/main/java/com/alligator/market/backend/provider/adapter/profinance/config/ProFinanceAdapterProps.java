package com.alligator.market.backend.provider.adapter.profinance.config;

import jakarta.validation.constraints.NotBlank;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

@Validated
@ConfigurationProperties("provider.connection-config.profinance")
public record ProFinanceAdapterProps(
        @NotBlank String baseUrl
) {
}
