package com.alligator.market.backend.source.adapter.twelvedata.instrument.forex.spot.properties;

import jakarta.validation.constraints.NotBlank;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

@Validated
@ConfigurationProperties("market-data-source.connection-properties.twelve-data.fx-spot")
public record TwelveDataFxSpotConnectionProperties(
        @NotBlank String baseUrl,
        @NotBlank String apiKey
) {
}
