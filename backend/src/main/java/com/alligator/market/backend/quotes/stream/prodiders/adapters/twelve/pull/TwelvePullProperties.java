package com.alligator.market.backend.quotes.stream.prodiders.adapters.twelve.pull;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Настройки для обращения к API TwelveData.
 */
@Component
@ConfigurationProperties(prefix = "twelvedata")
@Data
public class TwelvePullProperties {

    /**
     * Название провайдера
     */
    private String provider;

    /** Базовый URL API. */
    private String baseUrl;

    /** API-ключ. */
    private String apikey;
}
