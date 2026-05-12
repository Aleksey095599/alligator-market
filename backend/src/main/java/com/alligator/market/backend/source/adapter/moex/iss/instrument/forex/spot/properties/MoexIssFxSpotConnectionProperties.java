package com.alligator.market.backend.source.adapter.moex.iss.instrument.forex.spot.properties;

import jakarta.validation.constraints.NotBlank;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

@Validated
@ConfigurationProperties("market-source.connection-properties.moex.iss.fx-spot")
public record MoexIssFxSpotConnectionProperties(@NotBlank String baseUrl) {
}
