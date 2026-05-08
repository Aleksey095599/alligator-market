package com.alligator.market.backend.source.config.adapter.moex.iss.instrument.forex.spot.web;

import com.alligator.market.backend.source.adapter.moex.iss.instrument.forex.spot.properties.MoexIssFxSpotConnectionProperties;
import com.alligator.market.backend.source.config.infra.handler.web.HandlerBaseWebClientConfig;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.web.reactive.function.client.WebClient;

/**
 * Wiring configuration for the MOEX ISS FOREX_SPOT handler WebClient.
 */
@Configuration(proxyBeanMethods = false)
@Import(HandlerBaseWebClientConfig.class)
@EnableConfigurationProperties(MoexIssFxSpotConnectionProperties.class)
public class MoexIssFxSpotWebClientConfig {

    public static final String BEAN_NAME = "moexIssFxSpotWebClient";

    /**
     * WebClient bean for the MOEX ISS FOREX_SPOT handler.
     *
     * @param globalWebClient shared handler WebClient
     * @param props MOEX ISS FOREX_SPOT connection properties
     */
    @Bean(BEAN_NAME)
    public WebClient moexIssFxSpotWebClient(
            @Qualifier(HandlerBaseWebClientConfig.BEAN_HANDLER_BASE_WEB_CLIENT) WebClient globalWebClient,
            MoexIssFxSpotConnectionProperties props
    ) {
        return globalWebClient.mutate()
                .baseUrl(props.baseUrl())
                .build();
    }
}
