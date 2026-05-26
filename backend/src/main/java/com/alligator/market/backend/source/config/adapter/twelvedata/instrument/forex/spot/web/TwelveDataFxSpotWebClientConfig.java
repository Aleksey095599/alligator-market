package com.alligator.market.backend.source.config.adapter.twelvedata.instrument.forex.spot.web;

import com.alligator.market.backend.source.adapter.twelvedata.instrument.forex.spot.properties.TwelveDataFxSpotConnectionProperties;
import com.alligator.market.backend.source.config.infra.handler.web.HandlerBaseWebClientConfig;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration(proxyBeanMethods = false)
@Import(HandlerBaseWebClientConfig.class)
@EnableConfigurationProperties(TwelveDataFxSpotConnectionProperties.class)
public class TwelveDataFxSpotWebClientConfig {
    public static final String BEAN_NAME = "twelveDataFxSpotWebClient";

    @Bean(BEAN_NAME)
    public WebClient twelveDataFxSpotWebClient(
            @Qualifier(HandlerBaseWebClientConfig.BEAN_HANDLER_BASE_WEB_CLIENT) WebClient globalWebClient,
            TwelveDataFxSpotConnectionProperties props
    ) {
        return globalWebClient.mutate()
                .baseUrl(props.baseUrl())
                .build();
    }
}
